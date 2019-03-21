package sbs.controller.prodcomp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.model.x3.X3BomItem;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("prodcomp")
public class ProductionComponentsController {

	@Autowired
	MessageSource messageSource;
	@Autowired
	JdbcOracleX3Service x3Service;

	@RequestMapping("/main")
	public String view() {
		// TODO clear
		long start = System.currentTimeMillis();
		// Map<String, List<X3BomItem>> allBoms =
		// x3Service.getAllBomPartsTopLevel("ATW");
		// System.out.println(allBoms.size());
		// Map<String, Double> result =
		// getComponentsQuantitiesMultilevel(allBoms, "GSK225095P5P");
		// System.out.println(result);
		System.out.println((System.currentTimeMillis() - start) / 1000);
		return "prodcomp/main";
	}

	@RequestMapping("/make")
	public String doMake(Model model, Locale locale, RedirectAttributes redirectAttrs)
			throws FileNotFoundException, IOException {
		long start = System.currentTimeMillis();
		// get file
		File file = (File) model.asMap().get("file");
		if (file != null) {
			// file exist
			Map<String, Integer> fileInfo = new TreeMap<>();
			// READ FILE
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line;
				String code;
				int quantity;
				String[] split;
				int lineNo = 0;
				while ((line = br.readLine()) != null) {
					lineNo++;
					split = line.split(";");
					// structure
					if (split.length != 2) {
						redirectAttrs.addFlashAttribute("error",
								messageSource.getMessage("error.bad.file.structure", null, locale) + ". "
										+ messageSource.getMessage("general.line", null, locale) + " " + lineNo + ": "
										+ line);
						br.close();
						file.delete();
						return "redirect:/prodcomp/main";
					}
					// values
					try {
						code = split[0].toUpperCase().trim();
						quantity = Math.abs(Integer.parseInt(split[1]));
						if(fileInfo.containsKey(code)){
							fileInfo.put(code, fileInfo.get(code)+quantity);
						}
						else{
							fileInfo.put(code, quantity);
						}
					} catch (NumberFormatException ex) {
						redirectAttrs.addFlashAttribute("error",
								messageSource.getMessage("error.mustbeanumber", null, locale) + ". "
										+ messageSource.getMessage("general.line", null, locale) + " " + lineNo + ": "
										+ line);
						br.close();
						file.delete();
						return "redirect:/prodcomp/main";
					}
				}
				br.close();
				file.delete();
			}
			
			// DO CALCULATION
			// get all boms
			Map<String, Double> allComponents = new TreeMap<>();
			Map<String, Double> subComponents;
			
			Map<String, List<X3BomItem>> allBoms = x3Service.getAllBomPartsTopLevel("ATW");
			
			for(Map.Entry<String, Integer> main: fileInfo.entrySet()){
				subComponents = getComponentsQuantitiesMultilevel(allBoms, main.getKey());
				for(Map.Entry<String, Double> sub: subComponents.entrySet()){
					if(allComponents.containsKey(sub.getKey())){
						allComponents.put(sub.getKey(), 
								allComponents.get(sub.getKey()) + 
								(sub.getValue() * main.getValue())
						);
					}
					else{
						allComponents.put(sub.getKey(), sub.getValue() * main.getValue());
					}
				}
			}
			
			model.addAttribute("components", allComponents);
		} else {
			// no file
			redirectAttrs.addFlashAttribute("main", messageSource.getMessage("action.choose.file", null, locale));
			return "redirect:/prodcomp/main";
		}
		
		
		System.out.println((System.currentTimeMillis() - start) / 1000);
		return "prodcomp/main";
	}

	private Map<String, Double> getComponentsQuantitiesMultilevel(Map<String, List<X3BomItem>> allBoms,
			String itemCode) {
		Map<String, Double> resultMap = new TreeMap<>();

		List<X3BomItem> list = findBomPartsByParentCode(allBoms, itemCode);
		// x3Service.findBomPartsByParent("ATW", itemCode);

		String code;
		double qty;
		List<X3BomItem> subList;
		Map<String, Double> subMap;

		for (X3BomItem item : list) {
			code = item.getPartCode();
			qty = item.getModelQuantity();

			subList = findBomPartsByParentCode(allBoms, code);
			// x3Service.findBomPartsByParent("ATW", code);
			// TODO delete if isempty, do this below 
			if (subList.isEmpty()) {
				if (resultMap.containsKey(code)) {
					resultMap.put(code, resultMap.get(code) + qty);
				} else {
					resultMap.put(code, qty);
				}
			} else {
				subMap = getComponentsQuantitiesMultilevel(allBoms, code);
				for (Map.Entry<String, Double> entry : subMap.entrySet()) {
					if (resultMap.containsKey(entry.getKey())) {
						resultMap.put(entry.getKey(),
								resultMap.get(entry.getKey()) + item.getModelQuantity() * entry.getValue());
					} else {
						resultMap.put(entry.getKey(), item.getModelQuantity() * entry.getValue());
					}
				}
			}
		}
		return resultMap;
	}

	private List<X3BomItem> findBomPartsByParentCode(Map<String, List<X3BomItem>> allBoms, String itemCode) {
		List<X3BomItem> list = new ArrayList<>();
		for (Map.Entry<String, List<X3BomItem>> entry : allBoms.entrySet()) {
			if (entry.getKey().equals(itemCode)) {
				return entry.getValue();
			}
		}
		return list;
	}

	/*
	 * @RequestMapping(value = "/makelist", params = { "viewlist" }, method =
	 * RequestMethod.POST) public String viewList(@Valid OrdersToolsForm
	 * ordersToolsForm, BindingResult bindingResult, Model model,
	 * RedirectAttributes redirectAttrs, Locale locale) { if
	 * (bindingResult.hasErrors()) { return "ordtools/main"; }
	 * 
	 * // database list // Map<String, Integer> stock = //
	 * x3Service.findGeneralStockForAllProducts("ATW"); List<X3SalesOrderItem>
	 * lines =
	 * x3Service.findAllSalesOrdersAfvItemsInPeriod(ordersToolsForm.getStartDate
	 * (), ordersToolsForm.getEndDate(), "ATW");
	 * 
	 * List<X3KeyValString> allBomParts = x3Service.getAllBomPartsInBoms("ATW");
	 * List<X3ToolEntry> allTools = x3Service.getAllToolsInRouting("ATW");
	 * Map<String, Set<String>> bomPartsByCode = prepareMapByCode(allBomParts);
	 * Map<String, List<X3ToolEntry>> toolsOperations =
	 * prepareToolsOperations(allTools); // ^List<X3ToolEntry> // loop through
	 * lines OrdersToolsLine line; List<OrdersToolsLine> list = new
	 * ArrayList<>(); Set<X3ToolEntry> set;
	 * 
	 * 
	 * for (X3SalesOrderItem sois : lines) { set = new HashSet<>();
	 * fillEmptySetWithToolsByProductCode(sois.getProductCode(), set,
	 * bomPartsByCode, toolsOperations); if (set.size() > 0) { for (X3ToolEntry
	 * tool : set) { line = createOrdersToolsLineFromX3Info(sois);
	 * line.setTool(tool); list.add(line); } } else { line =
	 * createOrdersToolsLineFromX3Info(sois); line.setTool(new X3ToolEntry());
	 * list.add(line); } }
	 * 
	 * model.addAttribute("list", list); model.addAttribute("startDate",
	 * ordersToolsForm.getStartDate()); model.addAttribute("endDate",
	 * ordersToolsForm.getEndDate());
	 * 
	 * return "ordtools/main"; }
	 * 
	 * private Map<String, Set<String>> prepareMapByCode(List<X3KeyValString>
	 * pairs) { Map<String, Set<String>> map = new HashMap<>(); Set<String> set;
	 * for (X3KeyValString pair : pairs) { if (map.containsKey(pair.getKey())) {
	 * map.get(pair.getKey()).add(pair.getValue()); } else { set = new
	 * HashSet<>(); set.add(pair.getValue()); map.put(pair.getKey(), set); } }
	 * return map; }
	 * 
	 * private Map<String, List<X3ToolEntry>>
	 * prepareToolsOperations(List<X3ToolEntry> toolsList) { Map<String,
	 * List<X3ToolEntry>> map = new HashMap<>(); List<X3ToolEntry> list; for
	 * (X3ToolEntry entry : toolsList) { if (map.containsKey(entry.getCode())) {
	 * map.get(entry.getCode()).add(entry); } else { list = new ArrayList<>();
	 * list.add(entry); map.put(entry.getCode(), list); }
	 * 
	 * } return map; }
	 * 
	 * private void fillEmptySetWithToolsByProductCode(String code,
	 * Set<X3ToolEntry> set, Map<String, Set<String>> bomPartsByCode,
	 * Map<String, List<X3ToolEntry>> toolsOperations) {
	 * 
	 * // Tools if (toolsOperations.containsKey(code)) { for (X3ToolEntry tool :
	 * toolsOperations.get(code)) { set.add(tool); } }
	 * 
	 * // BOM if (bomPartsByCode.containsKey(code)) { for (String cpnitmref :
	 * bomPartsByCode.get(code)) { fillEmptySetWithToolsByProductCode(cpnitmref,
	 * set, bomPartsByCode, toolsOperations); } } }
	 * 
	 * private OrdersToolsLine createOrdersToolsLineFromX3Info(X3SalesOrderItem
	 * sois) { OrdersToolsLine otl = new OrdersToolsLine();
	 * otl.setOrder(sois.getOrderNumber());
	 * otl.setDemandedDate(sois.getDemandedDate());
	 * otl.setProductCode(sois.getProductCode());
	 * otl.setProductDescription(sois.getProductDescription());
	 * otl.setQuantity(sois.getQuantityOrdered()); return otl;
	 * 
	 * }
	 */
}

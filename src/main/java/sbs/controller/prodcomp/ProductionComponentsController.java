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

import sbs.helpers.TextHelper;
import sbs.model.x3.X3BomItem;
import sbs.model.x3.X3Product;
import sbs.service.geode.JdbcOracleGeodeService;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("prodcomp")
public class ProductionComponentsController {

	@Autowired
	MessageSource messageSource;
	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	JdbcOracleGeodeService geodeService;
	@Autowired
	TextHelper textHelper;

	@RequestMapping("/main")
	public String view() {
		return "prodcomp/main";
	}

	@RequestMapping("/make")
	public String doMake(Model model, Locale locale, RedirectAttributes redirectAttrs)
			throws FileNotFoundException, IOException {
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

			//Map<String, String> descriptions = x3Service.getDescriptionsByLanguage(JdbcOracleX3Service.LANG_POLISH, "ATW");
			Map<String, Double> quantities = x3Service.getAllProductsQuantities("ATW");
			Map<String, Double> geodeStock = geodeService.getStockOnProductionAndReceptions();
			Map<String, X3Product> products =  x3Service.findAllActiveProductsMap("ATW");
			
			List<List<String>> table = new ArrayList<>();
			List<String> line;
			double x3, qty, geode;
			for(Map.Entry<String, Double> entry: allComponents.entrySet()){

				x3 = quantities.getOrDefault(entry.getKey(), 0.0);
						//quantities.containsKey(entry.getKey()) ? quantities.get(entry.getKey()) : 0;
				qty = entry.getValue();
				geode = geodeStock.getOrDefault(entry.getKey(), 0.0);

				line = new ArrayList<>();
				line.add(entry.getKey());
				line.add(products.containsKey(entry.getKey()) ? products.get(entry.getKey()).getDescription() : "-");
				line.add(products.containsKey(entry.getKey()) ? products.get(entry.getKey()).getCategory() : "-");
				line.add(products.containsKey(entry.getKey()) ? products.get(entry.getKey()).getGr2() : "-");
				line.add(textHelper.numberFormatIntegerRoundSpace(x3));				
				line.add(textHelper.numberFormatIntegerRoundSpace(geode));												
				line.add(textHelper.numberFormatIntegerRoundSpace(qty));
				if(x3-qty >= 0){
					line.add(textHelper.numberFormatIntegerRoundSpace(0.0));
				}
				else{
					line.add(textHelper.numberFormatIntegerRoundSpace(Math.abs(x3-qty)));
				}
				table.add(line);
			}
			model.addAttribute("components", table);
		} else {
			// no file
			redirectAttrs.addFlashAttribute("main", messageSource.getMessage("action.choose.file", null, locale));
			return "redirect:/prodcomp/main";
		}
		
		return "prodcomp/main";
	}

	private Map<String, Double> getComponentsQuantitiesMultilevel(Map<String, List<X3BomItem>> allBoms,
			String itemCode) {
		Map<String, Double> resultMap = new TreeMap<>();

		List<X3BomItem> list = findBomPartsByParentCode(allBoms, itemCode);
		// x3Service.findBomPartsByParent("ATW", itemCode);

		String code;
		double qty;
		Map<String, Double> subMap;

		for (X3BomItem item : list) {
			code = item.getPartCode();
			qty = item.getModelQuantity();

			if (resultMap.containsKey(code)) {
				resultMap.put(code, resultMap.get(code) + qty);
			} else {
				resultMap.put(code, qty);
			}
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

}

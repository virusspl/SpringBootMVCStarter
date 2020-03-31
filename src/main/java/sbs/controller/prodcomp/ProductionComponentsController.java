package sbs.controller.prodcomp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.helpers.TextHelper;
import sbs.model.x3.X3BomItem;
import sbs.model.x3.X3BomPart;
import sbs.model.x3.X3ConsumptionProductInfo;
import sbs.model.x3.X3Product;
import sbs.model.x3.X3SalesOrderLine;
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
	public String view(Model model) {
		FormComponent formComponent = new FormComponent();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		formComponent.setStartDate(new Timestamp(cal.getTimeInMillis()));

		cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		formComponent.setEndDate(new Timestamp(cal.getTimeInMillis()));

		model.addAttribute("formComponent", formComponent);
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
						if (fileInfo.containsKey(code)) {
							fileInfo.put(code, fileInfo.get(code) + quantity);
						} else {
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
			Map<String, Double> quantities = x3Service.getAllProductsQuantities("ATW");
			Map<String, Double> geodeStock = geodeService.getStockOnProductionAndReceptions();
			Map<String, X3Product> products = x3Service.findAllActiveProductsMap("ATW");
			// get acv info
			List<X3ConsumptionProductInfo> acvInfo = x3Service.getAcvListForConsumptionReport("ATW");
			// safety stock map
			Map<String, Integer> safetyStockMap = new TreeMap<>();
			Map<String, List<X3BomItem>> allBoms = x3Service.getAllBomPartsTopLevel("ATW");

			for (Map.Entry<String, Integer> main : fileInfo.entrySet()) {
				subComponents = getComponentsQuantitiesMultilevel(allBoms, main.getKey(), products, false);
				for (Map.Entry<String, Double> sub : subComponents.entrySet()) {
					if (allComponents.containsKey(sub.getKey())) {
						allComponents.put(sub.getKey(),
								allComponents.get(sub.getKey()) + (sub.getValue() * main.getValue()));
					} else {
						allComponents.put(sub.getKey(), sub.getValue() * main.getValue());
					}
				}
			}

			for (X3ConsumptionProductInfo info : acvInfo) {
				safetyStockMap.put(info.getProductCode(), info.getSafetyStock());
			}
			// replanish point map
			Map<String, Integer> replanishMap = new TreeMap<>();
			for (X3ConsumptionProductInfo info : acvInfo) {
				replanishMap.put(info.getProductCode(), info.getReorderPoint());
			}

			List<List<String>> table = new ArrayList<>();
			List<String> line;
			double x3, qty, geode;
			for (Map.Entry<String, Double> entry : allComponents.entrySet()) {

				x3 = quantities.getOrDefault(entry.getKey(), 0.0);
				// quantities.containsKey(entry.getKey()) ? quantities.get(entry.getKey()) : 0;
				qty = entry.getValue();
				geode = geodeStock.getOrDefault(entry.getKey(), 0.0);

				line = new ArrayList<>();
				line.add(entry.getKey());
				line.add(products.containsKey(entry.getKey()) ? products.get(entry.getKey()).getDescription() : "-");
				line.add(products.containsKey(entry.getKey()) ? products.get(entry.getKey()).getCategory() : "-");
				line.add(products.containsKey(entry.getKey()) ? products.get(entry.getKey()).getGr2() : "-");
				line.add(safetyStockMap.containsKey(entry.getKey())
						? textHelper.numberFormatIntegerRoundSpace(safetyStockMap.get(entry.getKey()))
						: "-");
				line.add(replanishMap.containsKey(entry.getKey())
						? textHelper.numberFormatIntegerRoundSpace(replanishMap.get(entry.getKey()))
						: "-");
				line.add(textHelper.numberFormatIntegerRoundSpace(x3));
				line.add(textHelper.numberFormatIntegerRoundSpace(geode));
				line.add(textHelper.numberFormatIntegerRoundSpace(qty));
				if (x3 - qty >= 0) {
					line.add(textHelper.numberFormatIntegerRoundSpace(0.0));
				} else {
					line.add(textHelper.numberFormatIntegerRoundSpace(Math.abs(x3 - qty)));
				}
				table.add(line);
			}
			model.addAttribute("components", table);
			model.addAttribute("title", messageSource.getMessage("general.list", null, locale));
		} else {
			// no file
			redirectAttrs.addFlashAttribute("main", messageSource.getMessage("action.choose.file", null, locale));
			return "redirect:/prodcomp/main";
		}

		return "prodcomp/view";
	}

	@RequestMapping("/makeplan")
	public String doMakePlan(Model model, Locale locale, RedirectAttributes redirectAttrs)
			throws FileNotFoundException, IOException {

		if (model.asMap().get("days") == null) {
			return "redirect:/prodcomp/main";
		}

		int days = (int) model.asMap().get("days");
		File file = (File) model.asMap().get("file");

		if (file != null) {
			// file exist
			List<PlanLine> fileInfo = new ArrayList<>();
			// Map<String, Integer> fileInfo = new TreeMap<>();
			// READ FILE
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line;
				String[] split;
				String order;
				String code;
				String description;
				String date;
				String clientName;
				String country;
				int quantity;
				double lineValue;

				int lineNo = 0;
				PlanLine planLine;

				while ((line = br.readLine()) != null) {
					lineNo++;
					split = line.split(";");
					// structure
					if (split.length != 8) {
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
						order = split[0].toUpperCase().trim();
						code = split[1].toUpperCase().trim();
						description = split[2].toUpperCase().trim();
						date = split[3].toUpperCase().trim();
						clientName = split[4].toUpperCase().trim();
						country = split[5].toUpperCase().trim();
						quantity = Math.abs(Integer.parseInt(split[6]));
						lineValue = Math.abs(Double.parseDouble(split[7].replace(',', '.')));

						planLine = new PlanLine();
						planLine.setOrder(order);
						planLine.setCode(code);
						planLine.setDescription(description);
						planLine.setDate(date);
						planLine.setClientName(clientName);
						planLine.setCountry(country);
						planLine.setQuantity(quantity);
						planLine.setLineValue(lineValue);
						fileInfo.add(planLine);
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
			Map<String, List<X3BomItem>> allBoms = x3Service.getAllBomPartsTopLevel("ATW");
			// get products info
			Map<String, X3Product> products = x3Service.findAllActiveProductsMap("ATW");
			// get general stockof all products
			Map<String, Integer> stock = x3Service.findGeneralStockForAllProducts("ATW");
			Map<String, Integer> initStock = new HashMap<>();
			for(Map.Entry<String, Integer> entry: stock.entrySet()) {
				initStock.put(entry.getKey(), entry.getValue());
			}
			// get acv info
			List<X3ConsumptionProductInfo> acvInfo = x3Service.getAcvListForConsumptionReport("ATW");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, days);
			Map<String, Double> expectedDelivery = x3Service.getExpectedDeliveriesByDate(cal.getTime(), "ATW");

			// set in acv info: stock + expected delivery
			Map<String, Integer> acvStock = new HashMap<>();
			for (X3ConsumptionProductInfo info : acvInfo) {
				if (expectedDelivery.containsKey(info.getProductCode())) {
					info.setStock(info.getStock() + expectedDelivery.get(info.getProductCode()).intValue());
				}
				acvStock.put(info.getProductCode(), info.getStock());
			}

			String code;
			// total requirement
			int qreq;
			// unit requirement
			double qunitreq;
			int qstock;
			int shortage;
			// max units that could be produced regarding shortages
			int maxProd;
			Map<String, Integer> shortageList = new HashMap<>();

			// calculate shortage
			for (PlanLine main : fileInfo) {
				// FOR ALL LINES IN FILE
				// set in lines: components requirement info
				main.setRequirements(getCurrentAcvRequirementQuantitiesByStock(allBoms, main.getCode(), products, stock,
						main.getQuantity()));
				maxProd = (int) main.getQuantity();
				for (Map.Entry<String, Double> req : main.getRequirements().entrySet()) {
					// FOR ALL REQUIREMENTS IN PRODUCT
					// decrease acv stock and save info if shortage
					code = req.getKey();
					qreq = req.getValue().intValue();
					qunitreq = req.getValue() / main.getQuantity();
					qstock = acvStock.getOrDefault(req.getKey(), 0);
					shortage = qreq - qstock;
					if (shortage > 0) {
						if (qstock / qunitreq < maxProd) {
							maxProd = (int) (qstock / qunitreq);
						}
						qstock = 0;
						main.addShortage(code, shortage);
						if (shortageList.containsKey(code)) {
							shortageList.put(code, shortageList.get(code) + shortage);
						} else {
							shortageList.put(code, shortage);
						}
					} else {
						qstock -= qreq;
					}
					acvStock.put(code, qstock);
				}
				main.setMaxProduction(maxProd);

			}
			List<List<String>> table = new ArrayList<>();
			List<String> line;
			String shortages;
			for (PlanLine main : fileInfo) {
				shortages = "";
				line = new ArrayList<>();
				line.add(main.getOrder());
				line.add(main.getCode());
				line.add(main.getDescription());
				line.add(main.getDate());
				line.add(main.getClientName());
				line.add(main.getCountry());
				line.add(main.getLineValue() + "");
				line.add(main.getShortageValue() + "");
				line.add(main.getQuantity() + "");
				line.add(main.getMaxProduction() + "");
				line.add(main.getShortageQuantity() + "");
				for (Map.Entry<String, Integer> sh : main.getShortage().entrySet()) {
					shortages += sh.getKey() + " (" + sh.getValue() + "); ";
				}
				line.add(shortages);
				table.add(line);
			}

			List<List<String>> shortageSummary = new ArrayList<>();
			List<String> shortageLine;
			for(Map.Entry<String, Integer> entry: shortageList.entrySet()) {
				shortageLine = new ArrayList<>();
				shortageLine.add(entry.getKey());
				shortageLine.add(products.get(entry.getKey()).getDescription());
				shortageLine.add(initStock.getOrDefault(entry.getKey(), 0)+"");
				shortageLine.add((expectedDelivery.getOrDefault(entry.getKey(), 0.0)).intValue()+"");
				shortageLine.add(entry.getValue()+"");
				shortageSummary.add(shortageLine);
			}
			
			model.addAttribute("days", days);
			model.addAttribute("shortage", shortageSummary);
			model.addAttribute("planlines", table);
			model.addAttribute("title", messageSource.getMessage("prodcomp.shortage.list", null, locale));
		} else {
			// no file
			redirectAttrs.addFlashAttribute("main", messageSource.getMessage("action.choose.file", null, locale));
			return "redirect:/prodcomp/main";
		}

		return "prodcomp/view";
	}

	/**
	 * 
	 * @param allBoms           all BOM info from X3
	 * @param code              main product code
	 * @param products          all X3 products general info
	 * @param stock             all X3 products stock
	 * @param quantityToProduce quantity of main product to calculate requirements
	 * @return
	 */
	private Map<String, Double> getCurrentAcvRequirementQuantitiesByStock(Map<String, List<X3BomItem>> allBoms,
			String itemCode, Map<String, X3Product> products, Map<String, Integer> stock, double quantityToProduce) {

		Map<String, Double> resultMap = new TreeMap<>();

		List<X3BomItem> list = findBomPartsByParentCode(allBoms, itemCode);

		String code;
		double qtyReq;
		int currentStock;
		Map<String, Double> subMap;
		for (X3BomItem item : list) {
			code = item.getPartCode();
			qtyReq = item.getModelQuantity() * quantityToProduce;
			currentStock = stock.getOrDefault(code, 0);

			// skip if empty line
			if (products.get(code) == null) {
				continue;
			}
			// skip if no stock verify flag
			if (!products.get(code).isVerifyStock()) {
				continue;
			}

			if (products.get(code).getCategory().equalsIgnoreCase("ACV")) {
				// add to requirements list if ACV
				if (resultMap.containsKey(code)) {
					resultMap.put(code, resultMap.get(code) + qtyReq);
				} else {
					resultMap.put(code, qtyReq);
				}
			} else {
				// for all production (AFV) codes:
				if (currentStock >= qtyReq) {
					// if there is stock for all current component req,
					// decrease stock and ignore subcomponents
					currentStock -= qtyReq;
					continue;
				} else {
					qtyReq = qtyReq - currentStock;
					currentStock = 0;
				}
				// SAVE NEW STOCK INFO
				stock.put(code, currentStock);

				subMap = getCurrentAcvRequirementQuantitiesByStock(allBoms, code, products, stock, qtyReq);
				for (Map.Entry<String, Double> entry : subMap.entrySet()) {
					if (resultMap.containsKey(entry.getKey())) {
						resultMap.put(entry.getKey(),
								resultMap.get(entry.getKey()) + entry.getValue());
					} else {
						resultMap.put(entry.getKey(), entry.getValue());
					}
				}
			}

		}
		return resultMap;
	}

	private Map<String, Double> getComponentsQuantitiesMultilevel(Map<String, List<X3BomItem>> allBoms, String itemCode,
			Map<String, X3Product> products, boolean acvOnly) {
		Map<String, Double> resultMap = new TreeMap<>();

		List<X3BomItem> list = findBomPartsByParentCode(allBoms, itemCode);
		// x3Service.findBomPartsByParent("ATW", itemCode);

		String code;
		double qty;
		Map<String, Double> subMap;
		boolean toAdd;
		for (X3BomItem item : list) {
			code = item.getPartCode();
			qty = item.getModelQuantity();
			if (acvOnly) {
				if (products.get(code) == null) {
					continue;
				}
				if (products.get(code).getCategory().equalsIgnoreCase("ACV")) {
					toAdd = true;
				} else {
					toAdd = false;
				}
			} else {
				toAdd = true;
			}
			if (toAdd) {
				if (resultMap.containsKey(code)) {
					resultMap.put(code, resultMap.get(code) + qty);
				} else {
					resultMap.put(code, qty);
				}
			}
			subMap = getComponentsQuantitiesMultilevel(allBoms, code, products, acvOnly);
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

	@RequestMapping(value = "/findchains", params = { "find" }, method = RequestMethod.POST)
	public String findChains(@Valid FormComponent formComponent, BindingResult bindingResult, Model model,
			Locale locale, RedirectAttributes redirectAttrs) {

		// standard validation
		if (bindingResult.hasErrors()) {
			return "prodcomp/main";
		}

		// check if component exist
		String component;
		String componentDescription;
		formComponent.setComponent(formComponent.getComponent().toUpperCase());
		component = formComponent.getComponent();
		componentDescription = x3Service.findItemDescription("ATW", component);
		if (componentDescription == null) {
			bindingResult.rejectValue("component", "error.no.such.product", "ERROR");
			return "prodcomp/main";
		}

		// get sales orders
		List<X3SalesOrderLine> orders = x3Service.findOpenedSalesOrderLinesInPeriod(formComponent.getStartDate(),
				formComponent.getEndDate(), "ATW");
		if (orders.size() == 0) {
			bindingResult.rejectValue("startDate", "prodcomp.error.noordersfund", "ERROR");
			return "prodcomp/main";
		}

		// get BOM parts association [long]
		List<X3BomPart> allBoms = x3Service.getAllBomEntries("ATW");

		// prepare for quantities calculation loop
		List<List<X3BomPart>> allChains;
		List<X3BomPart> chain;

		X3BomPart initComponent = new X3BomPart();
		initComponent.setParentCode(component);
		initComponent.setPartCode(component);
		initComponent.setQuantityOfSubcode(1.0);
		initComponent.setQuantityOfSelf(1.0);

		// check if component is used in any BOM
		boolean found = false;
		for (X3BomPart part : allBoms) {
			if (initComponent.getParentCode().equalsIgnoreCase(part.getPartCode())) {
				found = true;
			}
		}
		if (!found) {
			bindingResult.rejectValue("component", "prodcomp.error.notusedinbom", "ERROR");
			return "prodcomp/main";
		}

		// prepare calculation variables
		allChains = new ArrayList<>();
		chain = new ArrayList<>();
		chain.add(initComponent);

		// just do it (BOM calculation)
		calculateBomChains(initComponent, chain, allChains, allBoms);
		List<List<X3BomPart>> finalChains = reverseLists(allChains);
		calculateChainsQuantities(finalChains);

		// get general stock info
		Map<String, Integer> generalStock = x3Service.findGeneralStockForAllProducts("ATW");

		// create sales objects and calculate chains
		List<SalesLineAndChains> salesObjects = new ArrayList<>();
		SalesLineAndChains object;
		for (X3SalesOrderLine line : orders) {
			object = new SalesLineAndChains(line);
			for (List<X3BomPart> finalChain : finalChains) {
				if (finalChain.get(0).getParentCode().equalsIgnoreCase(line.getProductCode())) {
					fillCurrentStockInfo(finalChain, generalStock);
					object.addClonedAndCalculatedChain(finalChain);
				}
			}
			if (object.getChains().size() > 0) {
				salesObjects.add(object);
			}
		}

		model.addAttribute("salesObjects", salesObjects);
		/*
		 * for(SalesLineAndChains obj: salesObjects) { S
		 * ystem.out.println(obj.getLine()); for(List<X3BomPart> schain:
		 * obj.getChains()){ S ystem.out.println(chainToString(schain)); } }
		 */

		model.addAttribute("component", component);
		model.addAttribute("componentDescription", componentDescription);
		model.addAttribute("title", component);
		return "prodcomp/view";
	}

	private void fillCurrentStockInfo(List<X3BomPart> finalChain, Map<String, Integer> generalStock) {
		for (X3BomPart part : finalChain) {
			part.setCurrentStock(new Double(generalStock.getOrDefault(part.getParentCode(), 0)));
		}
	}

	/**
	 * chain has to be in correct order (after REV, main to detail)
	 * 
	 * @param chains
	 */
	private void calculateChainsQuantities(List<List<X3BomPart>> chains) {
		double tmpMulti = 1.0;
		for (List<X3BomPart> chain : chains) {
			tmpMulti = 1.0;
			for (X3BomPart part : chain) {
				part.setQuantityOfSelf(part.getQuantityOfSelf() * tmpMulti);
				tmpMulti = part.getQuantityOfSubcode() * part.getQuantityOfSelf();
			}
		}
	}

	private List<List<X3BomPart>> reverseLists(List<List<X3BomPart>> allChains) {
		List<List<X3BomPart>> revLists = new ArrayList<>();
		List<X3BomPart> revList;
		for (int i = 0; i < allChains.size(); i++) {
			revList = new ArrayList<>();
			for (int j = allChains.get(i).size() - 1; j >= 0; j--) {
				revList.add(allChains.get(i).get(j));
			}
			revLists.add(revList);
		}
		return revLists;

	}

	/**
	 * 
	 * @param component X3BomPart to add its parents to list
	 * @param chain     list chain for current part
	 * @param allChains list to save finished chain lists
	 * @param allBoms   list of all BOM entries to check dependencies
	 */
	private boolean calculateBomChains(X3BomPart component, List<X3BomPart> chain, List<List<X3BomPart>> allChains,
			List<X3BomPart> allBoms) {

		boolean found = false;
		List<X3BomPart> localChain;

		for (X3BomPart part : allBoms) {
			if (component.getParentCode().equalsIgnoreCase(part.getPartCode())) {
				found = true;
				localChain = cloneBomPartList(chain);
				localChain.add(part);

				if (!calculateBomChains(part, localChain, allChains, allBoms)) {
					allChains.add(localChain);
				}
			}
		}
		return found;
	}

	private List<X3BomPart> cloneBomPartList(List<X3BomPart> list) {
		List<X3BomPart> clone = new ArrayList<>();
		for (X3BomPart part : list) {
			clone.add(new X3BomPart(part));
		}
		return clone;
	}

}

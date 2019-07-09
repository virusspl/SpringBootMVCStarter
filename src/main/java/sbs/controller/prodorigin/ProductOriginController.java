package sbs.controller.prodorigin;

import java.util.ArrayList;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.helpers.TextHelper;
import sbs.model.x3.X3BomItem;
import sbs.model.x3.X3Product;
import sbs.model.x3.X3Supplier;
import sbs.model.x3.X3SupplyStatInfo;
import sbs.service.geode.JdbcOracleGeodeService;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("prodorigin")
public class ProductOriginController {

	// TODO ehcache size of policy
	// + order + BOM by ref date from order ...
	// + prizes + % of UE / imp

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
		model.addAttribute("prodOriginForm", new ProdOriginForm());
		return "prodorigin/main";
	}

	@RequestMapping("/analyze")
	public String performSearch(@Valid ProdOriginForm prodOriginForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) {
		String itemDesc;
		if (bindingResult.hasErrors()) {
			return "prodorigin/main";
		}

		prodOriginForm.setProduct(prodOriginForm.getProduct().toUpperCase().trim());
		itemDesc = x3Service.findItemDescription("ATW", prodOriginForm.getProduct());

		if (itemDesc == null) {
			bindingResult.rejectValue("product", "error.no.such.product", "ERROR");
			return "prodorigin/main";
		}

		// DO CALCULATION
		// get general data
		Map<String, List<X3BomItem>> allBoms = x3Service.getAllBomPartsTopLevel("ATW");
		Map<String, X3Product> allProducts = x3Service.findAllActiveProductsMap("ATW");
		// get specific data
		Map<String, X3BomItem> bomParts = getComponentsMultilevel(allBoms, prodOriginForm.getProduct());
		List<X3BomItem> acvBomParts = getAcvPartsOnly(bomParts, allProducts);

		List<ComponentInfo> componentsList = new ArrayList<>();
		List<X3Supplier> partSuppliersList;
		List<SupplyInfo> supplyInfoList;
		ComponentInfo componentInfo;
		SupplyInfo supplyLine;
		X3SupplyStatInfo statInfo;

		Map<String, SupVal> euValMap = new TreeMap<>();
		Map<String, SupVal> impValMap = new TreeMap<>();

		for (X3BomItem item : acvBomParts) {
			componentInfo = new ComponentInfo();
			componentInfo.setProduct(allProducts.get(item.getPartCode()));
			componentInfo.setQuantityInBom(item.getModelQuantity());
			componentInfo.setUnit(item.getModelUnit());
			componentInfo.setEuropeOnly(true);
			partSuppliersList = x3Service.findProductSuppliers("ATW", item.getPartCode());
			supplyInfoList = new ArrayList<>();
			for (X3Supplier supp : partSuppliersList) {
				supplyLine = new SupplyInfo();
				supplyLine.setSupplierCode(supp.getCode());
				supplyLine.setSupplierName(supp.getName());
				supplyLine.setSupplierCountry(supp.getCountry());
				statInfo = x3Service.getSupplyStatistics("ATW", item.getPartCode(), supp.getCode());
				supplyLine.setNumberOfReceptions(statInfo.getNumberOfReceptions());
				supplyLine.setPurchasedQuantity(statInfo.getPurchasedQuantity());
				supplyLine.setLastReceptionNumber(statInfo.getLastReceptionNumber());
				supplyLine.setLastReceptionDate(statInfo.getLastReceptionDate());
				supplyLine.setLastReceptionPrice(statInfo.getLastReceptionPrice());
				supplyInfoList.add(supplyLine);

				// count for eu / imp summary
				if (supplyLine.getNumberOfReceptions() > 0) {
					if (supp.isEuMember()) {
						euValMap.put(componentInfo.getProduct().getCode(), new SupVal(supplyLine.getSupplierCode(),
								1.0 * componentInfo.getQuantityInBom() * supplyLine.getLastReceptionPrice(), supp.isEuMember()));
					} else {
						impValMap.put(componentInfo.getProduct().getCode(), new SupVal(supplyLine.getSupplierCode(),
								1.0 * componentInfo.getQuantityInBom() * supplyLine.getLastReceptionPrice(), supp.isEuMember()));
						componentInfo.setEuropeOnly(false);
					}
				}

			}
			componentInfo.setSupplyInfo(supplyInfoList);
			componentsList.add(componentInfo);
		}

		double euGenVal = 0.0;
		double euEuVal = 0.0;
		double euImpVal = 0.0;
		
		double impGenVal = 0.0;
		double impEuVal = 0.0;
		double impImpVal = 0.0;

		Map<String, SupValFusion> euImpFusionMap = new TreeMap<>();

		String code;
		SupValFusion fusion;
		
		for (ComponentInfo comp : componentsList) {
			code = comp.getProduct().getCode();
			fusion = new SupValFusion();

			if (euValMap.containsKey(code)) {
				fusion.setEuropeLine(euValMap.get(code));
				euEuVal  = euEuVal  + euValMap.get(code).getValue();
				euGenVal = euGenVal + euValMap.get(code).getValue();
			} else if (impValMap.containsKey(code)) {
				fusion.setEuropeLine(impValMap.get(code));
				euImpVal = euImpVal + impValMap.get(code).getValue();
				euGenVal = euGenVal + impValMap.get(code).getValue();
			}

			if (impValMap.containsKey(code)) {
				fusion.setImportLine(impValMap.get(code));
				impImpVal = impImpVal + impValMap.get(code).getValue();
				impGenVal = impGenVal + impValMap.get(code).getValue();
			} else if (euValMap.containsKey(code)) {
				fusion.setImportLine(euValMap.get(code));
				impEuVal  = impEuVal  + euValMap.get(code).getValue();
				impGenVal = impGenVal + euValMap.get(code).getValue();
			}
			
			euImpFusionMap.put(code,  fusion);
		}
		
		model.addAttribute("euGenVal",euGenVal);
		model.addAttribute("euEuVal",euEuVal);
		model.addAttribute("euImpVal",euImpVal);
		model.addAttribute("euEuProc",euEuVal*100.0/euGenVal);
		model.addAttribute("euImpProc",euImpVal*100.0/euGenVal);
		model.addAttribute("impGenVal",impGenVal);
		model.addAttribute("impEuVal",impEuVal);
		model.addAttribute("impImpVal",impImpVal);
		model.addAttribute("impImpProc",impImpVal*100.0/impGenVal);
		model.addAttribute("impEuProc",impEuVal*100.0/impGenVal);
		model.addAttribute("fusion", euImpFusionMap);
		model.addAttribute("productCode", prodOriginForm.getProduct());
		model.addAttribute("productDescription", itemDesc);
		model.addAttribute("components", componentsList);

		return "prodorigin/result";
	}

	private List<X3BomItem> getAcvPartsOnly(Map<String, X3BomItem> bomParts, Map<String, X3Product> products) {
		ArrayList<X3BomItem> list = new ArrayList<>();
		for (X3BomItem item : bomParts.values()) {
			if (products.containsKey(item.getPartCode())
					&& products.get(item.getPartCode()).getCategory().equals("ACV")) {
				list.add(item);
			}
		}
		return list;
	}

	private Map<String, X3BomItem> getComponentsMultilevel(Map<String, List<X3BomItem>> allBoms, String itemCode) {
		Map<String, X3BomItem> resultMap = new TreeMap<>();

		List<X3BomItem> list = findBomPartsByParentCode(allBoms, itemCode);

		Map<String, X3BomItem> subMap;

		for (X3BomItem item : list) {
			resultMap.put(item.getPartCode(), item);
			subMap = getComponentsMultilevel(allBoms, item.getPartCode());
			for (Map.Entry<String, X3BomItem> entry : subMap.entrySet()) {
				resultMap.put(entry.getKey(), entry.getValue());
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

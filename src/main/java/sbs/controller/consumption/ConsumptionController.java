package sbs.controller.consumption;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import sbs.helpers.DateHelper;
import sbs.helpers.ExcelContents;
import sbs.helpers.ExcelExportHelper;
import sbs.helpers.TextHelper;
import sbs.model.x3.X3ConsumptionProductInfo;
import sbs.model.x3.X3ConsumptionSupplyInfo;
import sbs.model.x3.X3CoverageData;
import sbs.model.x3.X3Supplier;
import sbs.model.x3.X3UsageDetail;
import sbs.service.x3.JdbcOracleX3Service;


@Controller
@RequestMapping("consumption")
public class ConsumptionController {
	
	@Autowired 
	JdbcOracleX3Service x3Service;
	@Autowired
	MessageSource messageSource;
	@Autowired
	TextHelper textHelper;
	@Autowired
	DateHelper dateHelper;

    public ConsumptionController() {
    	
    }
    
    @RequestMapping("/dispatch")
    public String dispatch(){
    	return "consumption/dispatch";
    }
    
    @RequestMapping("/exportCoverage")
    public ModelAndView exportCoverage(Model model, Locale locale){
    	Calendar cal = Calendar.getInstance();
    	String stamp = dateHelper.formatYyyyMmDdDot(cal.getTime());
    	cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)-1);
    	cal.set(Calendar.MONTH, Calendar.JANUARY);
    	cal.set(Calendar.DAY_OF_MONTH, 1);
    	
    	List<X3CoverageData> initialData = x3Service.getCoverageInitialData("ATW");
    	Map<String, String> descriptions = x3Service.getDescriptionsByLanguage(x3Service.convertLocaleToX3Lang(locale), "ATW");
		Map<String, Integer> demand = x3Service.getAcvDemandList("ATW");
		List<X3UsageDetail> usage = x3Service.getAcvUsageDetailsListByYear(cal.get(Calendar.YEAR), "ATW");
		Map<String, X3Supplier> suppliers = x3Service.getFirstAcvSuppliers("ATW");
    	
		// create headers
		ArrayList<String> headers = new ArrayList<>();
		headers.add("Product");
		headers.add("Description");
		headers.add("Group 2");
		headers.add("Reord. pol.");
		headers.add("Time");
		headers.add("Stock");
		headers.add("Require.");
		headers.add("Qty ord.");
		headers.add("Available");
		headers.add("Theor. st.");
		headers.add("In route");
		headers.add("Use in "+cal.get(Calendar.YEAR));		
		headers.add("Av. month use");
		headers.add("Av. Q I");
		headers.add("Av. Q II");
		headers.add("Av. Q III");
		headers.add("Av. Q IV");
		headers.add("How many months");
		headers.add("Cover 1");
		headers.add("Cover 2");
		headers.add("Suppl. code");
		headers.add("Oldest suppl. name");
		headers.add("First supply date");
		headers.add("Buyer code");
		headers.add("Buyer name");

		// create structure
		ArrayList<ArrayList<Object>> rows = new ArrayList<>();
		ArrayList<Object> lineValues;
		CoverageLine cLine;

		
		for(X3CoverageData line: initialData){
			cLine = makeLineFromData(line, descriptions, demand, usage, suppliers, cal.get(Calendar.YEAR));
			lineValues = makeExcelLineFromCoverageLine(cLine);
			rows.add(lineValues);
		}
		
    	// create excel contents
		ExcelContents contents = new ExcelContents();
		
		// set contents
		contents.setFileName("COVERAGE_"+stamp+".xls");
		contents.setSheetName(stamp);
		contents.setHeaders(headers);
		contents.setValues(rows);
		
		return new ModelAndView(new ExcelExportHelper(), "contents", contents);
    	

    }
    

	private CoverageLine makeLineFromData(X3CoverageData line, Map<String, String> descriptions,
			Map<String, Integer> demand, List<X3UsageDetail> usage, Map<String, X3Supplier> suppliers, int year) {
    	
    	CoverageLine cLine = new CoverageLine();
    	
    	// base info
    	cLine.setProductCode(line.getCode());
    	cLine.setDescription(descriptions.containsKey(cLine.getProductCode()) ? descriptions.get(cLine.getProductCode()) : "-");
    	cLine.setGr2(line.getGr2());
    	cLine.setTime(line.getTime());
    	cLine.setStock(line.getStock());
    	cLine.setRequired(demand.containsKey(cLine.getProductCode()) ? demand.get(cLine.getProductCode()) : 0);
    	cLine.setOrdered(line.getInOrder());
    	cLine.setAvailable(cLine.getStock() - cLine.getRequired());
    	cLine.setTheoretical(cLine.getAvailable() + cLine.getOrdered());
    	cLine.setInRoute(line.getInRoute());
    	cLine.setReorderPolicy(line.getReorderPolicy());
    	if(suppliers.containsKey(cLine.getProductCode())){
    		cLine.setSupplierCode(suppliers.get(cLine.getProductCode()).getCode());
    		cLine.setSupplierName(suppliers.get(cLine.getProductCode()).getName());
    		cLine.setFirstSupplyDate(suppliers.get(cLine.getProductCode()).getFirstOrderDate());
    	}
    	else{
    		cLine.setSupplierCode("");
    		cLine.setSupplierName("");
    	}
    	
		
    	// usage stat
		int[] usedMonths = new int[] {0,0,0,0,0,0,0,0,0,0,0,0};
		int currentMonth;
		int totalUsage = 0;
		int u1 = 0;
		int u2 = 0;
		int u3 = 0;
		int u4 = 0;
		
		// loop usage
		for (X3UsageDetail us : usage) {
			if (us.getProductCode().equals(cLine.getProductCode())) {

				currentMonth = dateHelper.extractMonth12(us.getUsageDate());
				usedMonths[(currentMonth - 1)] = 1;
				totalUsage += us.getUsage();
				if (currentMonth <= 3) {
					u1 += us.getUsage();
				} else if (currentMonth <= 6) {
					u2 += us.getUsage();
				} else if (currentMonth <= 9) {
					u3 += us.getUsage();
				} else {
					u4 += us.getUsage();
				}
			}
		}
		
		// usage data
		cLine.setUseInYear(totalUsage);
		cLine.setAverageMonthUse(totalUsage*1.0/12);
		cLine.setQ1(u1*1.0/3);
		cLine.setQ2(u2*1.0/3);
		cLine.setQ3(u3*1.0/3);
		cLine.setQ4(u4*1.0/3);
		cLine.setMonthsCount(IntStream.of(usedMonths).sum());
		cLine.setCover1(cLine.getUseInYear() == 0 ? 0.0 : cLine.getTheoretical()*1.0/cLine.getAverageMonthUse());
		cLine.setCover2(cLine.getUseInYear() == 0 ? 0.0 : (cLine.getAvailable()+cLine.getInRoute())*1.0/cLine.getAverageMonthUse());
		cLine.setBuyerCode(line.getBuyerCode());
		cLine.setBuyerName(line.getBuyerName());
		
		return cLine;
	}

	private ArrayList<Object> makeExcelLineFromCoverageLine(CoverageLine cLine) {
    	ArrayList<Object> lineValues = new ArrayList<>();
		lineValues.add(cLine.getProductCode());
		lineValues.add(cLine.getDescription());			
		lineValues.add(cLine.getGr2());
		lineValues.add(cLine.getReorderPolicy());
		lineValues.add(cLine.getTime());
		lineValues.add(cLine.getStock());
		lineValues.add(cLine.getRequired());
		lineValues.add(cLine.getOrdered());
		lineValues.add(cLine.getAvailable());
		lineValues.add(cLine.getTheoretical());
		lineValues.add(cLine.getInRoute());
		lineValues.add(cLine.getUseInYear());
		lineValues.add(cLine.getAverageMonthUse());
		lineValues.add(cLine.getQ1());
		lineValues.add(cLine.getQ2());
		lineValues.add(cLine.getQ3());
		lineValues.add(cLine.getQ4());
		lineValues.add(cLine.getMonthsCount());
		lineValues.add(cLine.getCover1());
		lineValues.add(cLine.getCover2());
		lineValues.add(cLine.getSupplierCode());
		lineValues.add(cLine.getSupplierName());
		lineValues.add(cLine.getFirstSupplyDate()!=null ? cLine.getFirstSupplyDate() : "");
		lineValues.add(cLine.getBuyerCode());
		lineValues.add(cLine.getBuyerName());
		return lineValues;
	}

	@RequestMapping("/exportConsumption/{company}")
    public ModelAndView exportConsumption(@PathVariable("company") String company, Model model, Locale locale){
    	
		if(!company.equals("ATW") && !company.equals("WPS")){
			model.addAttribute("error",  messageSource.getMessage("error.company.unknown", null, locale) + ": " + company);
			return new ModelAndView("consumption/dispatch");
		}
		
    	// get year
    	Calendar cal = Calendar.getInstance();
    	int consInitYear = cal.get(Calendar.YEAR);
    	
    	// get data
    	Map<String, Map<Integer, Integer>> consumption0 = x3Service.getAcvConsumptionListForYear((consInitYear-2), company);
    	Map<String, Map<Integer, Integer>> consumption1 = x3Service.getAcvConsumptionListForYear((consInitYear-1), company);
    	Map<String, Map<Integer, Integer>> consumption2 = x3Service.getAcvConsumptionListForYear(consInitYear, company);
    	Map<String, Integer> demand = x3Service.getAcvDemandList(company);
    	Map<String, String> enDescriptions = x3Service.getAcvProductsEnglishDescriptions(company);
    	Map<String, X3ConsumptionSupplyInfo> lastSupply = x3Service.getAcvListOfLastSupplyInfo(company);
    	List<X3ConsumptionProductInfo> productsList = x3Service.getAcvListForConsumptionReport(company);
    	Map<String, String> buyGroups = x3Service.getVariousTableData(company, "6050", JdbcOracleX3Service.LANG_ITALIAN);

    	
		// create headers
		ArrayList<String> headers = new ArrayList<>();
		headers.add("Product");
		headers.add("Descr PL");
		headers.add("Descr EN");
		headers.add("Pur. Fam. Code");
		headers.add("Purchasing family");		
		headers.add("Reorder point");
		headers.add("Safety stock");
		headers.add("Max stock");
		headers.add("EWZ");
		headers.add("Techn. lot");
		headers.add("Stock");
		headers.add("Avg cost");
		headers.add("Demand");
		headers.add("In order");
		headers.add("Buyer");
		headers.add("Lead time");
		headers.add("Last issue");
		headers.add("Last suppl.");
		headers.add("Last suppl. nam.");
		headers.add("Last suppl. date");
		for(int i = 1; i<=12; i++){
			headers.add((consInitYear-1) +"." + textHelper.fillWithLeadingZero(i+"", 2));
		}
		for(int i = 1; i<=12; i++){
			headers.add((consInitYear) +"." + textHelper.fillWithLeadingZero(i+"", 2));
		}
		headers.add("Consumption " + (consInitYear-2));
		headers.add("Consumption " + (consInitYear-1));
		headers.add("Consumption " + consInitYear);

		// create structure
		ArrayList<ArrayList<Object>> rows = new ArrayList<>();
		ArrayList<Object> lineValues;
		Calendar date1600 = Calendar.getInstance();
		date1600.set(Calendar.YEAR, 1600);
		
		for(X3ConsumptionProductInfo product: productsList){
			lineValues = new ArrayList<>();
			lineValues.add(product.getProductCode());
			lineValues.add(product.getProductDescriptionPl());
			lineValues.add(enDescriptions.containsKey(product.getProductCode()) ? enDescriptions.get(product.getProductCode()) : "" );
			lineValues.add(product.getBuyGroupCode());
			lineValues.add(buyGroups.containsKey(product.getBuyGroupCode()) ? buyGroups.get(product.getBuyGroupCode()) : "" );
			lineValues.add(product.getReorderPoint());
			lineValues.add(product.getSafetyStock());
			lineValues.add(product.getMaxStsock());
			lineValues.add(product.getEwz());
			lineValues.add(product.getTechnicalLot());
			lineValues.add(product.getStock());
			lineValues.add(product.getAverageCost());
			lineValues.add(demand.containsKey(product.getProductCode()) ? demand.get(product.getProductCode()) : "" );
			lineValues.add(product.getInOrder());
			lineValues.add(product.getBuyerCode());
			lineValues.add(product.getLeadTime());
			if(product.getLastIssueDate().before(date1600.getTime())){
				lineValues.add("");
			}
			else{
				lineValues.add(product.getLastIssueDate());
			}
			lineValues.add(lastSupply.containsKey(product.getProductCode()) ? lastSupply.get(product.getProductCode()).getSupplierCode() : "" );
			lineValues.add(lastSupply.containsKey(product.getProductCode()) ? lastSupply.get(product.getProductCode()).getName() : "" );
			lineValues.add(lastSupply.containsKey(product.getProductCode()) ? lastSupply.get(product.getProductCode()).getDate() : "" );
			int tmpPeriod; 
			int prevPrevSum = 0;
			// y -2 sum
			if (consumption0.containsKey(product.getProductCode())) {
				for(Map.Entry<Integer, Integer> entry: consumption0.get(product.getProductCode()).entrySet()){
					prevPrevSum+= entry.getValue();
				}
			}

			int prevSum = 0;
			int currSum = 0;
			for (int i = 1; i <= 12; i++) {
				if (consumption1.containsKey(product.getProductCode())) {
					tmpPeriod = Integer.parseInt((consInitYear - 1) + textHelper.fillWithLeadingZero(i + "", 2));
					if (consumption1.get(product.getProductCode()).containsKey(tmpPeriod)) {
						lineValues.add(consumption1.get(product.getProductCode()).get(tmpPeriod));
						prevSum += consumption1.get(product.getProductCode()).get(tmpPeriod);
					} else {
						lineValues.add("");
					}
				}
				else{
					lineValues.add("");
				}
			}
			for (int i = 1; i <= 12; i++) {
				if (consumption2.containsKey(product.getProductCode())) {
					tmpPeriod = Integer.parseInt(consInitYear + textHelper.fillWithLeadingZero(i + "", 2));
					if (consumption2.get(product.getProductCode()).containsKey(tmpPeriod)) {
						lineValues.add(consumption2.get(product.getProductCode()).get(tmpPeriod));
						currSum += consumption2.get(product.getProductCode()).get(tmpPeriod);
					} else {
						lineValues.add("");
					}
				}
				else{
					lineValues.add("");
				}
			}
			lineValues.add(prevPrevSum);
			lineValues.add(prevSum);
			lineValues.add(currSum);

			rows.add(lineValues);
		}
		
    	// create excel contents
		ExcelContents contents = new ExcelContents();
		
		// set contents
		contents.setFileName("CONS_" + company + "_" +consInitYear+"_EXPORT.xls");
		contents.setSheetName(consInitYear+"");
		contents.setHeaders(headers);
		contents.setValues(rows);
		
		return new ModelAndView(new ExcelExportHelper(), "contents", contents);
    }
    
    
}

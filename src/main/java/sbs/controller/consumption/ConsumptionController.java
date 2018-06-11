package sbs.controller.consumption;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import sbs.helpers.ExcelContents;
import sbs.helpers.ExcelExportHelper;
import sbs.helpers.TextHelper;
import sbs.model.x3.X3ConsumptionProductInfo;
import sbs.model.x3.X3ConsumptionSupplyInfo;
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

    public ConsumptionController() {
    	
    }
	
    @RequestMapping("/export")
    public ModelAndView view(Model model, Locale locale){
    	
    	// get year
    	Calendar cal = Calendar.getInstance();
    	int consInitYear = cal.get(Calendar.YEAR);
    	
    	// get data
    	Map<String, Integer> consumption1 = x3Service.getAcvConsumptionListForYear((consInitYear-1), "ATW");
    	Map<String, Integer> consumption2 = x3Service.getAcvConsumptionListForYear(consInitYear, "ATW");
    	Map<String, Integer> demand = x3Service.getAcvDemandList("ATW");
    	Map<String, String> enDescriptions = x3Service.getAcvProductsEnglishDescriptions("ATW");
    	Map<String, X3ConsumptionSupplyInfo> lastSupply = x3Service.getAcvListOfLastSupplyInfo("ATW");
    	List<X3ConsumptionProductInfo> productsList = x3Service.getAcvListForConsumptionReport("ATW");

		// create headers
		ArrayList<String> headers = new ArrayList<>();
		headers.add("Product");
		headers.add("Descr PL");
		headers.add("Descr EN");
		headers.add("Stock");
		headers.add("Avg cost");
		headers.add("Demand");
		headers.add("In order");
		headers.add("Last suppl.");
		headers.add("Last suppl. nam.");
		headers.add("Last suppl. date");
		headers.add("Consumption " + (consInitYear-1));
		headers.add("Consumption " + consInitYear);
		
		
		// create structure
		ArrayList<ArrayList<Object>> rows = new ArrayList<>();
		ArrayList<Object> lineValues;
		
		for(X3ConsumptionProductInfo product: productsList){
			lineValues = new ArrayList<>();
			lineValues.add(product.getProductCode());
			lineValues.add(product.getProductDescriptionPl());
			lineValues.add(enDescriptions.containsKey(product.getProductCode()) ? enDescriptions.get(product.getProductCode()) : "" );
			lineValues.add(product.getStock());
			lineValues.add(product.getAverageCost());
			lineValues.add(demand.containsKey(product.getProductCode()) ? demand.get(product.getProductCode()) : "" );
			lineValues.add(product.getInOrder());
			lineValues.add(lastSupply.containsKey(product.getProductCode()) ? lastSupply.get(product.getProductCode()).getSupplierCode() : "" );
			lineValues.add(lastSupply.containsKey(product.getProductCode()) ? lastSupply.get(product.getProductCode()).getName() : "" );
			lineValues.add(lastSupply.containsKey(product.getProductCode()) ? lastSupply.get(product.getProductCode()).getDate() : "" );
			lineValues.add(consumption1.containsKey(product.getProductCode()) ? consumption1.get(product.getProductCode()) : "" );
			lineValues.add(consumption2.containsKey(product.getProductCode()) ? consumption2.get(product.getProductCode()) : "" );
			rows.add(lineValues);
		}
		
    	// create excel contents
		ExcelContents contents = new ExcelContents();
		
		// set contents
		contents.setFileName("CONS_"+consInitYear+"_EXPORT");
		contents.setSheetName(consInitYear+"");
		contents.setHeaders(headers);
		contents.setValues(rows);
		
		return new ModelAndView(new ExcelExportHelper(), "contents", contents);
    }
    
    
}

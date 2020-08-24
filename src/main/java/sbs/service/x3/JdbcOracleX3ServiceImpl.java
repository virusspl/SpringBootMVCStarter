package sbs.service.x3;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import sbs.controller.dirrcpship.DirectReceptionsShipmentLine;
import sbs.controller.prodcomp.NoBomCodeInfo;
import sbs.model.proprog.Project;
import sbs.model.wpslook.WpslookRow;
import sbs.model.x3.X3AvgPriceLine;
import sbs.model.x3.X3BomItem;
import sbs.model.x3.X3BomPart;
import sbs.model.x3.X3Client;
import sbs.model.x3.X3ConsumptionProductInfo;
import sbs.model.x3.X3ConsumptionSupplyInfo;
import sbs.model.x3.X3CoverageData;
import sbs.model.x3.X3DeliverySimpleInfo;
import sbs.model.x3.X3EnvironmentInfo;
import sbs.model.x3.X3KeyValString;
import sbs.model.x3.X3Product;
import sbs.model.x3.X3ProductEventsHistory;
import sbs.model.x3.X3ProductFinalMachine;
import sbs.model.x3.X3ProductSellDemand;
import sbs.model.x3.X3ProductionOrderDetails;
import sbs.model.x3.X3PurchaseOrder;
import sbs.model.x3.X3RouteLine;
import sbs.model.x3.X3SaleInfo;
import sbs.model.x3.X3SalesOrder;
import sbs.model.x3.X3SalesOrderItem;
import sbs.model.x3.X3SalesOrderLine;
import sbs.model.x3.X3ShipmentMovement;
import sbs.model.x3.X3ShipmentStockLineWithPrice;
import sbs.model.x3.X3StandardCostEntry;
import sbs.model.x3.X3StoreInfo;
import sbs.model.x3.X3Supplier;
import sbs.model.x3.X3SupplyStatInfo;
import sbs.model.x3.X3ToolEntry;
import sbs.model.x3.X3UsageDetail;
import sbs.model.x3.X3UtrFault;
import sbs.model.x3.X3UtrFaultLine;
import sbs.model.x3.X3UtrMachine;
import sbs.model.x3.X3UtrWorker;
import sbs.model.x3.X3WarehouseWeightDetailLine;
import sbs.model.x3.X3WarehouseWeightLine;
import sbs.model.x3.X3Workstation;
import sbs.repository.x3.JdbcOracleX3Repository;

@Service
public class JdbcOracleX3ServiceImpl implements JdbcOracleX3Service {
	@Autowired
	JdbcOracleX3Repository jdbcOracleX3Repository;
	
	@Override
	public String convertLocaleToX3Lang(Locale locale) {
		String x3lang;
		switch (locale.getLanguage()) {
		case "en":
			x3lang = "ENG";
		case "it":
			x3lang = "ITA";
		default:
			x3lang = "POL";
		}
		return x3lang;
	}
	
	@Override
	@Cacheable(value="x3AllUsers")
	public List<String> findAllUsers(String company){
		return jdbcOracleX3Repository.findAllUsers(company);
	}

	@Override
	@Cacheable(value="x3ItemDescByCode")
	public String findItemDescription(String company, String product) {
		return jdbcOracleX3Repository.findItemDescription(company, product);
	}

	@Override
	@Cacheable(value="x3AllClients")
	public List<X3Client> findAllClients(String company) {
		return jdbcOracleX3Repository.findAllClients(company);
	}

	@Override
	@Cacheable(value="x3AllSalesOrders")
	public List<X3SalesOrder> findAllSalesOrders(String company) {
		return jdbcOracleX3Repository.findAllSalesOrders(company);
	}

	@Override
	@Cacheable(value="x3OpenedSalesOrders")
	public List<X3SalesOrder> findOpenedSalesOrders(String company) {
		return jdbcOracleX3Repository.findOpenedSalesOrders(company);
	}

	@Override
	@Cacheable(value="x3AllActiveProducts")
	public List<X3Product> findAllActiveProducts(String company) {
		return jdbcOracleX3Repository.findAllActiveProducts(company);
	}
	
	@Override
	@Cacheable(value="x3AllActiveProductsMap")
	public Map<String, X3Product> findAllActiveProductsMap(String company) {
		return jdbcOracleX3Repository.findAllActiveProductsMap(company);
	}

	@Override
	public X3Product findProductByCode(String company, String code) {
		return jdbcOracleX3Repository.findProductByCode(company, code);
	}

	@Override
	public X3Client findClientByCode(String company, String code) {
		return jdbcOracleX3Repository.findClientByCode(company, code);
	}

	@Override
	public X3SalesOrder findSalesOrderByNumber(String company, String number) {
		return jdbcOracleX3Repository.findSalesOrderByNumber(company, number);
	}

	@Override
	public X3ProductionOrderDetails getProductionOrderInfoByNumber(String company, String number) {
		return jdbcOracleX3Repository.getProductionOrderInfoByNumber(company, number);
	}

	@Override
	public List<X3BomItem> findBomPartsByParent(String company, String productCode) {
		return jdbcOracleX3Repository.findBomPartsByParent(company, productCode);
	}

	@Override
	public List<WpslookRow> findLocationsOfProduct(String company, String code) {
		return jdbcOracleX3Repository.findLocationsOfProduct(company, code);
	}

	@Override
	public List<X3BomItem> findProductionPartsByProductionOrderAndOperation(String company, String productionOrder,
			int operationNumber) {
		return jdbcOracleX3Repository.findProductionPartsByProductionOrderAndOperation(company, productionOrder, operationNumber);
	}

	@Override
	public String findOperationDescriptionByProductionOrder(String company, String productionOrder, int operationNumber) {
		return jdbcOracleX3Repository.findOperationDescriptionByProductionOrder(company, productionOrder, operationNumber);
	}

	@Override
	public String findFinalClientByOrder(String company, String order) {
		return jdbcOracleX3Repository.findFinalClientByOrder(company, order);
	}

	@Override
	@Cacheable(value="x3AllUtrMachines")
	public Map<String, X3UtrMachine> findAllUtrMachines(String company) {
		return jdbcOracleX3Repository.findAllUtrMachines(company);
	}

	@Override
	@Cacheable(value="x3AllUtrWorkers")
	public Map<String, X3UtrWorker> findAllUtrWorkers(String company) {
		return jdbcOracleX3Repository.findAllUtrWorkers(company);
	}

	@Override
	public List<X3ShipmentMovement> findAdrShipmentMovementsInPeriod(Date startDate, Date endDate) {
		return jdbcOracleX3Repository.findAdrShipmentMovementsInPeriod(startDate, endDate);
	}

	@Override
	public Map<String, X3UtrFault> findUtrFaultsInPeriod(Date startDate, Date endDate) {
		return jdbcOracleX3Repository.findUtrFaultsInPeriod(startDate, endDate);
	}
	
	@Override
	@Cacheable(value="x3AllUtrFaults")
	public Map<String, X3UtrFault> findAllUtrFaults() {
		return jdbcOracleX3Repository.findAllUtrFaults();
	}
	
	@Override
	public X3UtrFault findUtrFault(String number) {
		return jdbcOracleX3Repository.findUtrFault(number);
	}

	@Override
	public List<X3UtrFaultLine> findUtrFaultLinesAfterDate(Date startDate) {
		return jdbcOracleX3Repository.findUtrFaultLinesAfterDate(startDate);
	}

	@Override
	@Cacheable(value="x3AllUtrFaultLines")
	public List<X3UtrFaultLine> findAllUtrFaultLines() {
		return jdbcOracleX3Repository.findAllUtrFaultLines();
	}

	@Override
	@Cacheable(value="x3productFinalMachines")
	public Map<String, X3ProductFinalMachine> findX3ProductFinalMachines(String company) {
		return jdbcOracleX3Repository.findX3ProductFinalMachines(company);
	}

	@Override
	//@Cacheable(value="x3PendingProjectsProgress")
	public List<Project> findPendingProjectsProgress() {
		return jdbcOracleX3Repository.findPendingProjectsProgress();
	}

	@Override
	public Project findProjectProgressByNumber(String number) {
		return jdbcOracleX3Repository.findProjectProgressByNumber(number);
	}

	@Override
	public List<X3WarehouseWeightLine> findWeightSumLine(Date startDate, Date endDate, int weightQueryType) {
		return jdbcOracleX3Repository.findWeightSumLine(startDate, endDate, weightQueryType);
	}

	@Override
	public List<X3WarehouseWeightDetailLine> findWeightDetailLine(Date startDate, Date endDate, int weightQueryType) {
		return jdbcOracleX3Repository.findWeightDetailLine(startDate, endDate, weightQueryType);
	}


	@Override
	public Map<String, Integer> findAcvMagStock(String company) {
		return jdbcOracleX3Repository.findAcvMagStock(company);
	}
	
	@Override
	public Map<String, Integer> findAcvShipStock(String company) {
		return jdbcOracleX3Repository.findAcvShipStock(company);
	}
	@Override
	public List<X3ShipmentStockLineWithPrice> findAllShipStockWithAveragePrice(String company) {
		return jdbcOracleX3Repository.findAllShipStockWithAveragePrice(company);
	}

	@Override
	public List<String> findAcvNonProductionCodes(String company) {
		return jdbcOracleX3Repository.findAcvNonProductionCodes(company);
	}

	@Override
	public Map<String, X3ProductSellDemand> findAcvProductSellDemand(Date startDate, Date endDate, String company) {
		return jdbcOracleX3Repository.findAcvProductSellDemand(startDate, endDate, company);
	}

	@Override
	public Map<String, Integer> findAcvAverageUsageInPeriod(String startPeriod, String endPeriod, String company) {
		return jdbcOracleX3Repository.findAcvAverageUsageInPeriod(startPeriod, endPeriod, company);
	}

	@Override
	public List<DirectReceptionsShipmentLine> findDirectReceptionsShipmentLines(Date startDate, Date endDate, String company) {
		return jdbcOracleX3Repository.findDirectReceptionsShipmentLines(startDate,  endDate, company);
	}

	@Override
	public Map<String, Map<Integer, Integer>> getAcvConsumptionListForYear(int year, String company) {
		return jdbcOracleX3Repository.getAcvConsumptionListForYear(year, company);
	}

	@Override
	public Map<String, Integer> getAcvDemandList(String company) {
		return jdbcOracleX3Repository.getAcvDemandList(company);
	}

	@Override
	public Map<String, X3ConsumptionSupplyInfo> getAcvListOfLastSupplyInfo(String company) {
		return jdbcOracleX3Repository.getAcvListOfLastSupplyInfo(company);
	}

	@Override
	@Cacheable(value="acvInfo")
	public List<X3ConsumptionProductInfo> getAcvListForConsumptionReport(String company) {
		return jdbcOracleX3Repository.getAcvListForConsumptionReport(company);
	}

	@Override
	public Map<String, String> getAcvProductsEnglishDescriptions(String company) {
		return jdbcOracleX3Repository.getAcvProductsEnglishDescriptions(company);
	}

	@Override
	public List<X3SalesOrderLine> findOpenedSalesOrderLinesInPeriod(Date startDate, Date endDate, String company) {
		return jdbcOracleX3Repository.findOpenedSalesOrderLinesInPeriod(startDate, endDate, company);
	}

	@Override
	public Map<String, Integer> findGeneralStockForAllProducts(String company) {
		return jdbcOracleX3Repository.findGeneralStockForAllProducts(company);
	}

	@Override
	public X3Workstation findWorkstationByCode(String company, String code) {
		return jdbcOracleX3Repository.findWorkstationByCode(company, code);
	}

	@Override
	public boolean checkIfLocationExist(String company, String location) {
		return jdbcOracleX3Repository.checkIfLocationExist(company, location);
	}

	@Override
	public X3PurchaseOrder findPurchaseOrderByNumber(String company, String number) {
		return jdbcOracleX3Repository.findPurchaseOrderByNumber(company, number);
	}

	@Override
	public String findPackageDescription(String company, String packageCode) {
		return jdbcOracleX3Repository.findPackageDescription(company, packageCode);
	}

	@Override
	@Cacheable(value="x3Descriptions")
	public Map<String, String> getDescriptionsByLanguage(String x3lang, String company) {
		return jdbcOracleX3Repository.getDescriptionsByLanguage(x3lang, company);
	}


	@Override
	@Cacheable(value="x3AcvUsageDetails")
	public List<X3UsageDetail> getAcvUsageDetailsListByYear(int year, String company) {
		return jdbcOracleX3Repository.getAcvUsageDetailsListByYear(year, company); 
	}

	@Override
	public List<X3CoverageData> getCoverageInitialData(String company) {
		return jdbcOracleX3Repository.getCoverageInitialData(company); 
	}

	@Override
	@Cacheable(value="x3FirstSuppliers")
	public Map<String, X3Supplier> getFirstAcvSuppliers(String company) {
		return jdbcOracleX3Repository.getFirstAcvSuppliers(company);
	}

	@Override
	public List<X3SalesOrderItem> findAllSalesOrdersAfvItemsInPeriod(Date startDate, Date endDate, String company) {
		return jdbcOracleX3Repository.findAllSalesOrdersAfvItemsInPeriod(startDate, endDate, company);
	}

	@Override
	public List<X3ToolEntry> getAllToolsInRouting(String company) {
		return jdbcOracleX3Repository.getAllToolsInRouting(company);
	}

	@Override
	public List<X3KeyValString> getAllBomPartsInBoms(String company) {
		return jdbcOracleX3Repository.getAllBomPartsInBoms(company);
	}

	@Override
	public Map<String, X3StoreInfo> getX3StoreInfoByCode(String company) {
		return jdbcOracleX3Repository.getX3StoreInfoByCode(company);
	}

	@Override
	public Map<String, String> getVariousTableData(String company, String table, String x3language) {
		return jdbcOracleX3Repository.getVariousTableData(company, table, x3language);
	}

	@Override
	public Map<String, Integer> findStockByLocation(String company, String location) {
		return jdbcOracleX3Repository.findStockByLocation(company, location);
	}

	@Override
	@Cacheable(value="x3AllBomPartsTopLevel")
	public Map<String, List<X3BomItem>> getAllBomPartsTopLevel(String company) {
		return jdbcOracleX3Repository.getAllBomPartsTopLevel(company);
	}

	@Override
	@Cacheable(value="getAllBomEntries")	
	public List<X3BomPart> getAllBomEntries(String company) {
		return jdbcOracleX3Repository.getAllBomEntries(company);
	}

	@Override
	public Map<String, Double> getAllProductsQuantities(String company) {
		return jdbcOracleX3Repository.getAllProductsQuantities(company);
	}

	@Override
	public String updateStandardCostsTable(String company) {
		Map<String, X3StandardCostEntry> stdCostsUpdate = jdbcOracleX3Repository.getLastStandardCostsListFromCalculationTable(company);
		Map<String, X3StandardCostEntry> stdCostsCurrent = jdbcOracleX3Repository.getStandardCostsMap(company);
		List<X3StandardCostEntry> updateList = new ArrayList<>();
		List<X3StandardCostEntry> insertList = new ArrayList<>();
		
		for(Entry<String, X3StandardCostEntry> entry: stdCostsUpdate.entrySet()){
			if(stdCostsCurrent.containsKey(entry.getKey())){
				updateList.add(entry.getValue());
			}
			else{
				insertList.add(entry.getValue());
			}
		}

		jdbcOracleX3Repository.insertStandardCostsInQuickTable(updateList, insertList, company);
		
		return "Finished standard costs update for " + company + " (update: " + updateList.size() + ", insert: " + insertList.size()  + ")";		
	}

	@Override
	public Map<String, X3StandardCostEntry> getStandardCostsMap(String company) {
		return jdbcOracleX3Repository.getStandardCostsMap(company);
	}

	@Override
	public List<X3Supplier> findProductSuppliers(String company, String productCode) {
		return jdbcOracleX3Repository.findProductSuppliers(company, productCode);
	}

	@Override
	public X3SupplyStatInfo getSupplyStatistics(String company, String productCode, String supplierCode) {
		return jdbcOracleX3Repository.getSupplyStatistics(company, productCode, supplierCode);
	}

	@Override
	@Cacheable(value="x3RoutesMap")
	public Map<String, Map<Integer, X3RouteLine>> getRoutesMap(String company) {
			return jdbcOracleX3Repository.getRoutesMap(company);
	}

	@Override
	public Map<String, String> getWorkcenterNumbersMapByMachines(String company) {
		return jdbcOracleX3Repository.getWorkcenterNumbersMapByMachines(company);
	}

	@Override
	public Map<String, Integer> findGeneralMagStock(String company) {
		return jdbcOracleX3Repository.findGeneralMagStock(company);
	}

	@Override
	public Map<String, Integer> findGeneralShipStock(String company) {
		return jdbcOracleX3Repository.findGeneralShipStock(company);
	}

	@Override
	public Map<String, String> getPendingProductionOrdersBySaleOrders(String company) {
		return jdbcOracleX3Repository.getPendingProductionOrdersBySaleOrders(company);
	}

	@Override
	@Cacheable(value="x3Workstations")
	public List<X3Workstation> getWorkstations(String company) {
		return jdbcOracleX3Repository.getWorkstations(company);
	}

	@Override
	@Cacheable(value="x3StandardCostsMap")
	public Map<String, Double> getCurrentStandardCostsMap(String company) {
		return jdbcOracleX3Repository.getCurrentStandardCostsMap(company);
	}

	@Override
	public List<X3EnvironmentInfo> getEnvironmentInfoInPeriod(Date startDate, Date endDate, String type,
			String company) {
		return jdbcOracleX3Repository.getEnvironmentInfoInPeriod(startDate, endDate, type, company);
	}

	@Override
	public List<X3AvgPriceLine> getAveragePricesByInvoices(String company) {
		return jdbcOracleX3Repository.getAveragePricesByInvoices(company);
	}

	@Override
	public List<X3AvgPriceLine> getAveragePricesByOrders(String company) {
		return jdbcOracleX3Repository.getAveragePricesByOrders(company);
	}

	@Override
	public Map<String, Integer> findStockForAllProductsWithStock(String company) {
		return jdbcOracleX3Repository.findStockForAllProductsWithStock(company);
	}

	@Override
	public Map<String, Double> getExpectedDeliveriesByDate(Date date, String company) {
		return jdbcOracleX3Repository.getExpectedDeliveriesByDate(date, company);
	}

	@Override
	public Map<String, Date> getLatestExpectedDeliveryDateForCodeByDate(Date date, String company) {
		return jdbcOracleX3Repository.getLatestExpectedDeliveryDateForCodeByDate(date, company);
	}

	@Override
	public Map<String, X3DeliverySimpleInfo> getFirstUpcomingDeliveriesMapByCodeAfterDate(Date date, String company) {
		return jdbcOracleX3Repository.getFirstUpcomingDeliveriesMapByCodeAfterDate(date, company);
	}

	@Override
	public Map<String, X3DeliverySimpleInfo> getMostRecentDeliveriesMapByCodeBeforeDate(Date date, String company) {
		return jdbcOracleX3Repository.getMostRecentDeliveriesMapByCodeBeforeDate(date, company);
	}

	@Override
	public Map<String, Integer> findStockByState(String state, String company) {
		return jdbcOracleX3Repository.findStockByState(state, company);
	}

	@Override
	public Map<String, Integer> findProductsInReplenish(String company) {
		return jdbcOracleX3Repository.findProductsInReplenish(company);
	}

	@Override
	@Cacheable(value="acvProductsEventsHistory")
	public Map<String, X3ProductEventsHistory> getAcvProductsEventsHistory(Date startDate, Date endDate,
			List<X3ConsumptionProductInfo> acvInfo, String company) {
		Map<String, X3ProductEventsHistory> history = jdbcOracleX3Repository.getAcvProductsEventsHistory(startDate, endDate, acvInfo, company); 
		return history;
	}

	@Override
	public List<X3SalesOrderLine> findAdrSalesOrderLinesBasedOnShipmentMovementsInPeriod(Date startDate, Date endDate) {
		return jdbcOracleX3Repository.findAdrSalesOrderLinesBasedOnShipmentMovementsInPeriod(startDate, endDate);
	}

	@Override
	public List<NoBomCodeInfo> getNoBomCodesListIncompleteObjects(String company) {
		return jdbcOracleX3Repository.getNoBomCodesListIncompleteObjects(company);
	}

	@Override
	public Map<String, X3SaleInfo> getSaleInfoInPeriod(Date startDate, Date endDate, String company) {
		return jdbcOracleX3Repository.getSaleInfoInPeriod(startDate, endDate, company);
	}


}

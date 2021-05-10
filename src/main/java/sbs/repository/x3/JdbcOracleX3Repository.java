package sbs.repository.x3;
import java.util.Date;
// http://www.codesenior.com/en/tutorial/Spring-Generic-DAO-and-Generic-Service-Implementation
import java.util.List;
import java.util.Map;

import sbs.controller.dirrcpship.DirectReceptionsShipmentLine;
import sbs.controller.prodcomp.NoBomCodeInfo;
import sbs.model.generic.StringDoublePair;
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
import sbs.model.x3.X3HistockRawEntry;
import sbs.model.x3.X3KeyValString;
import sbs.model.x3.X3Product;
import sbs.model.x3.X3ProductEventsHistory;
import sbs.model.x3.X3ProductMachine;
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

public interface JdbcOracleX3Repository  {
	
    public List<String> findAllUsers(String company);
    public List<X3Product> findAllActiveProducts(String company);
	public Map<String, X3Product> findAllActiveProductsMap(String company);
	public X3Product findProductByCode(String company, String code);
    public String findItemDescription(String company, String code);
	public List<X3Client> findAllClients(String company);
	public X3Client findClientByCode(String company, String code);
	public List<X3SalesOrder> findAllSalesOrders(String company);
	public List<X3SalesOrder> findOpenedSalesOrders(String company);
	public X3SalesOrder findSalesOrderByNumber(String company, String number);
	public X3PurchaseOrder findPurchaseOrderByNumber(String company, String number);
	public X3ProductionOrderDetails getProductionOrderInfoByNumber(String company, String number);
	public List<X3BomItem> findBomPartsByParent(String company, String productCode);
	public List<WpslookRow> findLocationsOfProduct(String company, String code);
	public List<X3BomItem> findProductionPartsByProductionOrderAndOperation(String company, String productionOrder, int operationNumber);
	public String findOperationDescriptionByProductionOrder(String company, String productionOrder, int operationNumber);
	public String findFinalClientByOrder(String company, String order);
	public Map<String, X3UtrMachine> findAllUtrMachines(String company, int ysoce);
	public Map<String, X3UtrWorker> findAllUtrWorkers(String company);
	public List<X3ShipmentMovement> findAdrShipmentMovementsInPeriod(Date startDate, Date endDate);
	public Map<String, X3UtrFault> findUtrFaultsInPeriod(Date startDate, Date endDate);
	public Map<String, X3UtrFault> findAllUtrFaults();
	public X3UtrFault findUtrFault(String number);	
	public List<X3UtrFaultLine> findUtrFaultLinesAfterDate(Date startDate);
	public List<X3UtrFaultLine> findAllUtrFaultLines();
	public Map<String, X3ProductMachine> findX3ProductFinalMachines(String company);
	public Map<String, X3ProductMachine> findX3ProductFirstMachines(String company);
	public List<Project> findPendingProjectsProgress();
	public Project findProjectProgressByNumber(String number);
	public List<X3WarehouseWeightLine> findWeightSumLine(Date startDate, Date endDate, int weightQueryType);
	public List<X3WarehouseWeightDetailLine> findWeightDetailLine(Date startDate, Date endDate, int weightQueryType);
	public Map<String, Integer> findAcvAverageUsageInPeriod(String startPeriod, String endPeriod, String company);
	public Map<String, X3ProductSellDemand> findAcvProductSellDemand(Date startDate, Date endDate, String company);
	public List<String> findAcvNonProductionCodes(String company);
	public Map<String, Integer> findAcvMagStock(String company);
	public Map<String, Integer> findAcvShipStock(String company);
	public List<X3ShipmentStockLineWithPrice> findAllShipStockWithAveragePrice(String company);
	public List<DirectReceptionsShipmentLine> findDirectReceptionsShipmentLines(Date startDate, Date endDate, String company);
	public Map<String, Map<Integer, Integer>> getConsumptionListForYear(int year, String company);
	public Map<String, Integer> getDemandList(String company);
	public Map<String, Integer> getDemandListInPeriod(Date startDate, Date endDate, String company);
	public Map<String, X3ConsumptionSupplyInfo> getListOfLastSupplyInfo(String company);
	public List<X3ConsumptionProductInfo> getListForConsumptionReport(String category, String company);
	public Map<String, String> getProductsEnglishDescriptions(String company);
	public Map<String, Integer> findGeneralStockForAllProducts(String company);
	public Map<String, Integer> findStockByLocation(String company, String location);
	public Map<String, Integer> findStockByState(String state, String company);
	public List<X3SalesOrderLine> findOpenedSalesOrderLinesInPeriod(Date startDate, Date endDate, String company);
	public X3Workstation findWorkstationByCode(String company, String code);
	public boolean checkIfLocationExist(String company, String location);
	public String findPackageDescription(String company, String packageCode);
	public Map<String, String> getDescriptionsByLanguage(String x3lang, String company);
	public List<X3UsageDetail> getAcvUsageDetailsListByYear(int year, String company);
	public List<X3CoverageData> getCoverageInitialData(String company);
	public Map<String, X3Supplier> getFirstAcvSuppliers(String company);
	public List<X3SalesOrderItem> findAllSalesOrdersAfvItemsInPeriod(Date startDate, Date endDate, String company);
	public List<X3ToolEntry> getAllToolsInRouting(boolean filled, String company);
	public List<X3KeyValString> getAllBomPartsInBoms(String company);
	public Map<String, X3StoreInfo> getX3StoreInfoByCode(String company);
	public Map<String, String> getVariousTableData(String company, String table, String x3language);
	public Map<Integer, String> getX3Menu(String company, int menu, String x3language);
	public Map<String, List<X3BomItem>> getAllBomPartsTopLevel(String company);
	public List<X3BomPart> getAllBomEntries(String company);
	public Map<String, Double> getAllProductsQuantities(String company);
	public Map<String, X3StandardCostEntry> getLastStandardCostsListFromCalculationTable(String company);
	public Map<String, X3StandardCostEntry> getStandardCostsMap(String company);
	public void insertStandardCostsInQuickTable(List<X3StandardCostEntry> updateList,
			List<X3StandardCostEntry> insertList, String company);
	public List<X3Supplier> findProductSuppliers(String company, String productCode);
	public X3SupplyStatInfo getSupplyStatistics(String company, String productCode, String supplierCode);
	public Map<String, Map<Integer, X3RouteLine>> getRoutesMap(String company);
	public Map<String, String> getWorkcenterNumbersMapByMachines(String company);
	public Map<String, Integer> findGeneralMagStock(String company);
	public Map<String, Integer> findGeneralShipStock(String company);
	public Map<String, String> getPendingProductionOrdersBySaleOrders(String company);
	public List<X3Workstation> getWorkstations(String company);
	public Map<String, Double> getCurrentStandardCostsMap(String company);
	public List<X3EnvironmentInfo> getEnvironmentInfoInPeriod(Date startDate, Date endDate, String type,
			String company);
	public List<X3AvgPriceLine> getAveragePricesByInvoices(String company);
	public List<X3AvgPriceLine> getAveragePricesByOrders(String company);
	public Map<String, Integer> findStockForAllProductsWithStock(String company);
	public Map<String, Double> getExpectedDeliveriesByDate(Date date, String company);
	public Map<String, Date> getLatestExpectedDeliveryDateForCodeByDate(Date date, String company);
	public Map<String, X3DeliverySimpleInfo> getFirstUpcomingDeliveriesMapByCodeAfterDate(Date date, String company);
	public Map<String, X3DeliverySimpleInfo> getMostRecentDeliveriesMapByCodeBeforeDate(Date date, String company);
	public Map<String, Integer> findProductsInReplenish(String company);
	public Map<String, X3ProductEventsHistory> getAcvProductsEventsHistory(Date startDate, Date endDate, List<X3ConsumptionProductInfo> acvInfo, String company);
	public List<X3SalesOrderLine> findAdrSalesOrderLinesBasedOnShipmentMovementsInPeriod(Date startDate, Date endDate);
	public List<NoBomCodeInfo> getNoBomCodesListIncompleteObjects(String company);
	public Map<String, X3SaleInfo> getSaleInfoInPeriod(Date startDate, Date endDate, String company);
	public NoBomCodeInfo getNoBomCodeIncompleteObject(String code, String company);
	public List<X3HistockRawEntry> getHistockRawEntries(int years, String company);
	public List<X3HistockRawEntry> getHistockRawPendingEntries(int years, String company);
	/**
	 * average delivery deviation
	 */
	public void updateAverageDeliveryDaysInDatabase(List<StringDoublePair> list, String company);
	public List<String> getComponentSuppliers(String component, String company);
	public Map<String, String> findAllUsersMap(String company);
	public Map<String, Double> getAverageCostsMap(String company);
	
	
	
	
}

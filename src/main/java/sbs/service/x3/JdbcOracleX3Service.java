package sbs.service.x3;

import java.util.Date;
import java.util.List;
import java.util.Map;

import sbs.model.x3.X3BomItem;
import sbs.model.x3.X3Client;
import sbs.model.x3.X3Product;
import sbs.model.x3.X3ProductFinalMachine;
import sbs.model.x3.X3ProductionOrderDetails;
import sbs.model.x3.X3SalesOrder;
import sbs.model.x3.X3ShipmentMovement;
import sbs.model.x3.X3UtrFault;
import sbs.model.x3.X3UtrFaultLine;
import sbs.model.x3.X3UtrMachine;
import sbs.model.x3.X3UtrWorker;

public interface JdbcOracleX3Service {
	
    public List<String> findAllUsers(String company);
    public List<X3Product> findAllActiveProducts(String company);
	public X3Product findProductByCode(String company, String code);
    public String findItemDescription(String company, String code);
	public List<X3Client> findAllClients(String company);
	public X3Client findClientByCode(String company, String code);
	public List<X3SalesOrder> findAllSalesOrders(String company);
	public List<X3SalesOrder> findOpenedSalesOrders(String company);
	public X3SalesOrder findSalesOrderByNumber(String company, String number);
	public X3ProductionOrderDetails getProductionOrderInfoByNumber(String company, String number);
	public List<X3BomItem> findBomPartsByParent(String company, String productCode);
	public Object findLocationsOfProduct(String company, String code);
	public List<X3BomItem> findProductionPartsByProductionOrderAndOperation(String company, String productionOrder, int operationNumber);
	public String findOperationDescriptionByProductionOrder(String company, String order, int operation);
	public String findFinalClientByOrder(String company, String order);
	public List<X3ShipmentMovement> findAdrShipmentMovementsInPeriod(Date startDate, Date endDate);
	public Map<String, X3UtrMachine> findAllUtrMachines(String company);
	public Map<String, X3UtrWorker> findAllUtrWorkers(String company);
	public Map<String, X3UtrFault> findUtrFaultsInPeriod(Date startDate, Date endDate);
	public Map<String, X3UtrFault> findAllUtrFaults();
	public List<X3UtrFaultLine> findUtrFaultLinesAfterDate(Date startDate);
	public List<X3UtrFaultLine> findAllUtrFaultLines();
	public Map<String, X3ProductFinalMachine> findX3ProductFinalMachines(String company);
}

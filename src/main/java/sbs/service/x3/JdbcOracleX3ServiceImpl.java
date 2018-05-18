package sbs.service.x3;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import sbs.model.proprog.Project;
import sbs.model.wpslook.WpslookRow;
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
import sbs.model.x3.X3WarehouseWeightDetailLine;
import sbs.model.x3.X3WarehouseWeightLine;
import sbs.repository.x3.JdbcOracleX3Repository;

@Service
public class JdbcOracleX3ServiceImpl implements JdbcOracleX3Service {
	@Autowired
	JdbcOracleX3Repository jdbcOracleX3Repository;
	
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
	@Cacheable(value="x3PendingProjectsProgress")
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

}

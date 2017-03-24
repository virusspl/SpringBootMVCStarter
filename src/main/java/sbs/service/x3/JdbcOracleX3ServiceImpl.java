package sbs.service.x3;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import sbs.model.wpslook.WpslookRow;
import sbs.model.x3.X3BomItem;
import sbs.model.x3.X3Client;
import sbs.model.x3.X3Product;
import sbs.model.x3.X3ProductionOrderDetails;
import sbs.model.x3.X3SalesOrder;
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



}

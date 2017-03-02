package sbs.service.x3;

import java.util.List;

public interface JdbcOracleX3Service {
	public List<String> findAllUsers(String company);
	public List<X3Client> findAllClients(String company);
	public List<X3SalesOrder> findAllSalesOrders(String company);
	public List<X3SalesOrder> findOpenedSalesOrders(String company);
	public List<X3Product> findAllActiveProducts(String company);
	
	public String findItemDescription(String company, String product);
}

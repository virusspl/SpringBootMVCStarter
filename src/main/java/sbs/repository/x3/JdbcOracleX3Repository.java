package sbs.repository.x3;
// http://www.codesenior.com/en/tutorial/Spring-Generic-DAO-and-Generic-Service-Implementation
import java.util.List;

import sbs.model.x3.X3Client;
import sbs.model.x3.X3Product;
import sbs.model.x3.X3SalesOrder;

public interface JdbcOracleX3Repository  {
	
    public List<String> findAllUsers(String company);
    public List<X3Product> findAllActiveProducts(String company);
	public X3Product findProductByCode(String company, String code);
    public String findItemDescription(String company, String code);
	public List<X3Client> findAllClients(String company);
	public X3Client findClientByCode(String company, String code);
	public List<X3SalesOrder> findAllSalesOrders(String company);
	public List<X3SalesOrder> findOpenedSalesOrders(String company);
	public X3SalesOrder findSalesOrderByNumber(String company, String number);
	
	
}

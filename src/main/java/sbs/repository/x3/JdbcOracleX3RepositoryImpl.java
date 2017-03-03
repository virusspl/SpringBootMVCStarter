package sbs.repository.x3;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import sbs.model.x3.X3Client;
import sbs.model.x3.X3Product;
import sbs.model.x3.X3SalesOrder;


@Repository
public class JdbcOracleX3RepositoryImpl implements JdbcOracleX3Repository {

	 @Autowired 
	 @Qualifier("oracleX3JdbcTemplate") 
	 protected JdbcTemplate jdbc;

	@Override
	public List<String> findAllUsers(String company) {
        List<Map<String,Object>> resultSet = jdbc.queryForList(
                "SELECT " + company + ".AUTILIS.NOMUSR_0 FROM " + company + ".AUTILIS",
                new Object[]{});
        
        List<String> result = new ArrayList<>();
        for(Map<String,Object> row: resultSet ){
        	result.add((String)row.get("NOMUSR_0"));
        }
        
		return result;
	}

	@Override
	public String findItemDescription(String company, String code) {
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ company + ".ITMMASTER.ITMDES1_0 "
				+ "FROM " + company + ".ITMMASTER "
				+ "WHERE UPPER(" + company + ".ITMMASTER.ITMREF_0) = ? "
				, new Object[] {code.toUpperCase()});
        String result = null;
        for(Map<String,Object> row: resultSet ){
        	result = (String)row.get("ITMDES1_0");
        }
		return result;
	}
	
	@Override
	public List<X3Client> findAllClients(String company) {
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
						"SELECT " 
						+ company + ".BPCUSTOMER.BPCNUM_0, "
						+ company + ".BPARTNER.BPRNAM_0, "
						+ company + ".BPARTNER.BPRNAM_1 "
						+ "FROM " 
						+ company + ".BPCUSTOMER INNER JOIN " + company + ".BPARTNER "
						+ "ON "
						+ company + ".BPCUSTOMER.BPCNUM_0 = " + company + ".BPARTNER.BPRNUM_0 ",
                new Object[]{});
        
		List<X3Client> result = new ArrayList<>();
		X3Client client = null;
		
        for(Map<String,Object> row: resultSet ){
        	client = new X3Client();
        	client.setCode((String)row.get("BPCNUM_0"));
        	client.setName(((String)row.get("BPRNAM_0")) + " " + ((String)row.get("BPRNAM_1")));
        	result.add(client);
        }
		
		return result;
	}
	
	@Override
	public X3Client findClientByCode(String company, String code) {
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
						"SELECT " 
						+ company + ".BPCUSTOMER.BPCNUM_0, "
						+ company + ".BPARTNER.BPRNAM_0, "
						+ company + ".BPARTNER.BPRNAM_1 "
						+ "FROM " 
						+ company + ".BPCUSTOMER INNER JOIN " + company + ".BPARTNER "
						+ "ON "
						+ company + ".BPCUSTOMER.BPCNUM_0 = " + company + ".BPARTNER.BPRNUM_0 "
						+ "WHERE UPPER("
						+ company + ".BPARTNER.BPRNUM_0) = ?"
						+ "",
                new Object[]{code.toUpperCase()});
        		
		X3Client client = null;
		
        for(Map<String,Object> row: resultSet ){
        	client = new X3Client();
        	client.setCode((String)row.get("BPCNUM_0"));
        	client.setName(((String)row.get("BPRNAM_0")) + " " + ((String)row.get("BPRNAM_1")));
        }
		
		return client;
	}

	@Override
	public List<X3SalesOrder> findAllSalesOrders(String company) {

		List<Map<String,Object>> resultSet = jdbc.queryForList(
						"SELECT " 
						+ company + ".SORDER.SOHNUM_0, "
						+ company + ".SORDER.BPCORD_0, "
						+ company + ".BPCUSTOMER.BPCNAM_0, "
						+ company + ".SORDER.ORDDAT_0, "
						+ company + ".SORDER.ORDSTA_0 "
						+ "FROM " 
						+ company + ".BPCUSTOMER INNER JOIN " + company + ".SORDER "
						+ "ON "
						+ company + ".BPCUSTOMER.BPCNUM_0 = " + company + ".SORDER.BPCORD_0 ",
                new Object[]{});
        
		List<X3SalesOrder> result = new ArrayList<>();
		X3SalesOrder order = null;
		
        for(Map<String,Object> row: resultSet ){
        	order = new X3SalesOrder();
        	order.setSalesNumber((String)row.get("SOHNUM_0"));
        	order.setClientCode((String)row.get("BPCORD_0"));
        	order.setClientName((String)row.get("BPCNAM_0"));
        	order.setOrderDate((Timestamp)row.get("ORDDAT_0"));
        	if(((BigDecimal)row.get("ORDSTA_0"))==BigDecimal.valueOf(1)){
        		order.setOpened(true);
        	}
        	else{
        		order.setOpened(false);
        	}
        	result.add(order);
        }

		return result;
	}

	@Override
	public List<X3SalesOrder> findOpenedSalesOrders(String company) {
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
						"SELECT " 
						+ company + ".SORDER.SOHNUM_0, "
						+ company + ".SORDER.BPCORD_0, "
						+ company + ".BPCUSTOMER.BPCNAM_0, "
						+ company + ".SORDER.ORDDAT_0, "
						+ company + ".SORDER.ORDSTA_0 "
						+ "FROM " 
						+ company + ".BPCUSTOMER INNER JOIN " + company + ".SORDER "
						+ "ON "
						+ company + ".BPCUSTOMER.BPCNUM_0 = " + company + ".SORDER.BPCORD_0 "
						+ "WHERE "
						+ company + ".SORDER.ORDSTA_0 = 1",
                new Object[]{});
        
		List<X3SalesOrder> result = new ArrayList<>();
		X3SalesOrder order = null;
		
        for(Map<String,Object> row: resultSet ){
        	order = new X3SalesOrder();
        	order.setSalesNumber((String)row.get("SOHNUM_0"));
        	order.setClientCode((String)row.get("BPCORD_0"));
        	order.setClientName((String)row.get("BPCNAM_0"));
        	order.setOrderDate((Timestamp)row.get("ORDDAT_0"));
        	if(((BigDecimal)row.get("ORDSTA_0"))==BigDecimal.valueOf(1)){
        		order.setOpened(true);
        	}
        	else{
        		order.setOpened(false);
        	}
        	result.add(order);
        }
        
		return result;
	}
	
	@Override
	public X3SalesOrder findSalesOrderByNumber(String company, String number) {

		List<Map<String,Object>> resultSet = jdbc.queryForList(
						"SELECT " 
						+ company + ".SORDER.SOHNUM_0, "
						+ company + ".SORDER.BPCORD_0, "
						+ company + ".BPCUSTOMER.BPCNAM_0, "
						+ company + ".SORDER.ORDDAT_0, "
						+ company + ".SORDER.ORDSTA_0 "
						+ "FROM " 
						+ company + ".BPCUSTOMER INNER JOIN " + company + ".SORDER "
						+ "ON "
						+ company + ".BPCUSTOMER.BPCNUM_0 = " + company + ".SORDER.BPCORD_0 "
						+ "WHERE UPPER("
						+ company + ".SORDER.SOHNUM_0) = ? ",
                new Object[]{number.toUpperCase()});
        
		X3SalesOrder order = null;
		
        for(Map<String,Object> row: resultSet ){
        	order = new X3SalesOrder();
        	order.setSalesNumber((String)row.get("SOHNUM_0"));
        	order.setClientCode((String)row.get("BPCORD_0"));
        	order.setClientName((String)row.get("BPCNAM_0"));
        	order.setOrderDate((Timestamp)row.get("ORDDAT_0"));
        	if(((BigDecimal)row.get("ORDSTA_0"))==BigDecimal.valueOf(1)){
        		order.setOpened(true);
        	}
        	else{
        		order.setOpened(false);
        	}
        }
        
		return order;
	}

	@Override
	public List<X3Product> findAllActiveProducts(String company) {
	
		List<Map<String,Object>> resultSet = jdbc.queryForList(
						"SELECT " 
						+ company + ".ITMMASTER.ITMREF_0, "
						+ company + ".ITMMASTER.ITMDES1_0, "
						+ company + ".ITMMASTER.ITMDES2_0, "
						+ company + ".ITMMASTER.TCLCOD_0 "
						+ "FROM " 
						+ company + ".ITMMASTER "						
						+ "WHERE "
						+ company + ".ITMMASTER.ITMSTA_0 = 1",
                new Object[]{});
        
		List<X3Product> result = new ArrayList<>();
		X3Product product = null;
		
        for(Map<String,Object> row: resultSet ){
        	product = new X3Product();
        	product.setCode((String)row.get("ITMREF_0"));
        	product.setDescription(((String)row.get("ITMDES1_0")) + " " +  ((String)row.get("ITMDES2_0")));
        	product.setCategory((String)row.get("TCLCOD_0"));
        	result.add(product);
        }
		
		return result;
	}

	@Override
	public X3Product findProductByCode(String company, String code) {

		List<Map<String,Object>> resultSet = jdbc.queryForList(
						"SELECT " 
						+ company + ".ITMMASTER.ITMREF_0, "
						+ company + ".ITMMASTER.ITMDES1_0, "
						+ company + ".ITMMASTER.ITMDES2_0, "
						+ company + ".ITMMASTER.TCLCOD_0 "
						+ "FROM " 
						+ company + ".ITMMASTER "						
						+ "WHERE "
						+ company + ".ITMMASTER.ITMSTA_0 = 1 "
						+ "AND ("
						+ company + ".ITMMASTER.ITMREF_0) = ? ",
                new Object[]{code.toUpperCase()});
        
		X3Product product = null;
		
        for(Map<String,Object> row: resultSet ){
        	product = new X3Product();
        	product.setCode((String)row.get("ITMREF_0"));
        	product.setDescription(((String)row.get("ITMDES1_0")) + " " +  ((String)row.get("ITMDES2_0")));
        	product.setCategory((String)row.get("TCLCOD_0"));
        }
        
		return product;
	}


}

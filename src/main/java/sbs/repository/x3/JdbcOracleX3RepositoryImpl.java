package sbs.repository.x3;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import sbs.config.error.NotFoundException;
import sbs.controller.dirrcpship.DirectReceptionsShipmentLine;
import sbs.controller.prodcomp.NoBomCodeInfo;
import sbs.helpers.DateHelper;
import sbs.model.generic.StringIntPair;
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
import sbs.model.x3.X3ProductEvent;
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
import sbs.service.x3.JdbcOracleX3Service;

@Repository
public class JdbcOracleX3RepositoryImpl implements JdbcOracleX3Repository {

	protected JdbcTemplate jdbc;
	@Autowired 
	@Qualifier("oracleX3v6JdbcTemplate") 
	protected JdbcTemplate jdbc6;
	@Autowired 
	@Qualifier("oracleX3v11JdbcTemplate") 
	protected JdbcTemplate jdbc11;
	@Autowired
	private DateHelper dateHelper;
	
	private String x3v;
	
	public JdbcOracleX3RepositoryImpl(Environment env) {
		this.x3v = env.getRequiredProperty("oracleX3.dbVersion");
	}
	
	@Override
	public List<String> findAllUsers(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
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
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
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
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		
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
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
						"SELECT " 
						+ company + ".BPCUSTOMER.BPCNUM_0, "
						+ company + ".BPARTNER.BPRNAM_0, "
						+ company + ".BPARTNER.BPRNAM_1, "
						+ company + ".BPARTNER.CRY_0 "
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
        	client.setCountry((String)row.get("CRY_0"));
        }
		
		return client;
	}

	@Override
	public List<X3SalesOrder> findAllSalesOrders(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
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
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
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
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
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
	public X3PurchaseOrder findPurchaseOrderByNumber(String company, String number) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
						"SELECT " 
						+ company + ".PORDER.POHNUM_0, "
						+ company + ".PORDER.BPSNUM_0, "
						+ company + ".BPSUPPLIER.BPSNAM_0, "
						+ company + ".PORDER.ORDDAT_0, "
						+ company + ".PORDER.CLEFLG_0 "
						+ "FROM " 
						+ company + ".BPSUPPLIER INNER JOIN " + company + ".PORDER "
						+ "ON "
						+ company + ".BPSUPPLIER.BPSNUM_0 = " + company + ".PORDER.BPSNUM_0 "
						+ "WHERE UPPER("
						+ company + ".PORDER.POHNUM_0) = ? ",
                new Object[]{number.toUpperCase()});
        
		X3PurchaseOrder order = null;
		
        for(Map<String,Object> row: resultSet ){
        	order = new X3PurchaseOrder();
        	order.setPurchaseNumber((String)row.get("POHNUM_0"));
        	order.setSupplierCode((String)row.get("BPSNUM_0"));
        	order.setSupplierName((String)row.get("BPSNAM_0"));
        	order.setOrderDate((Timestamp)row.get("ORDDAT_0"));
        	if(((BigDecimal)row.get("CLEFLG_0"))==BigDecimal.valueOf(1)){
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
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
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
	public Map<String, X3Product> findAllActiveProductsMap(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT " 
						+ company + ".ITMMASTER.ITMREF_0, "
						+ company + ".ITMMASTER.ITMDES1_0, "
						+ company + ".ITMMASTER.ITMDES2_0, "
						+ company + ".ITMMASTER.XVGIAC_0, "
						+ company + ".ITMMASTER.TCLCOD_0, "
						+ company + ".ITMMASTER.TSICOD_1 "
						+ "FROM " 
						+ company + ".ITMMASTER "						
						+ "WHERE "
						+ company + ".ITMMASTER.ITMSTA_0 = 1",
						new Object[]{});
		
		Map<String, X3Product> result = new HashMap<>();
		X3Product product = null;
		boolean tmpBoolean; 
		for(Map<String,Object> row: resultSet ){
			product = new X3Product();
			product.setCode((String)row.get("ITMREF_0"));
			product.setDescription(((String)row.get("ITMDES1_0")) + " " +  ((String)row.get("ITMDES2_0")));
			product.setCategory((String)row.get("TCLCOD_0"));
			product.setGr2((String)row.get("TSICOD_1"));
			tmpBoolean = ((BigDecimal)row.get("XVGIAC_0")).intValue() == 2 ? true : false;
			product.setVerifyStock(tmpBoolean);
			
			result.put(product.getCode(), product);
		}
		
		return result;
	}
	
	@Override
	public Map<String, Double> getAllProductsQuantities(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT " 
						+ company + ".ITMMVT.ITMREF_0, "
						+ company + ".ITMMVT.PHYSTO_0 "
						+ "FROM " 
						+ company + ".ITMMVT "
						,
						new Object[]{});
		
		Map<String, Double> result = new HashMap<>();
		
		for(Map<String,Object> row: resultSet ){
			result.put(
					(String)row.get("ITMREF_0"), 
					((BigDecimal)row.get("PHYSTO_0")).doubleValue()
					);
		}
		
		return result;
	}

	@Override
	public X3Product findProductByCode(String company, String code) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
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

	@Override
	public X3ProductionOrderDetails getProductionOrderInfoByNumber(String company, String number) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT " 
				+ company + ".MFGITM.MFGNUM_0, "
				+ company + ".MFGITM.ITMREF_0, "
				+ company + ".MFGITM.EXTQTY_0, "
				+ company + ".ITMMASTER.ITMDES1_0, "
				+ company + ".ITMMASTER.ITMDES2_0, "
				+ company + ".SORDER.SOHNUM_0, "
				+ company + ".SORDER.XCLIORI_0, "
				+ company + ".BPARTNER.BPRNAM_0, "
				+ company + ".BPARTNER.BPRNAM_1 "
				+ "FROM (( " 
				+ company + ".MFGITM INNER JOIN " + company + ".ITMMASTER "
				+ "ON " 
				+ company + ".MFGITM.ITMREF_0 = " + company +  ".ITMMASTER.ITMREF_0 ) "
				+ "INNER JOIN "
				+ company + ".SORDER "
				+ "ON "
				+ company + ".MFGITM.PJT_0 = " + company + ".SORDER.SOHNUM_0) "
				+ "INNER JOIN "
				+ company + ".BPARTNER ON "
				+ company + ".SORDER.XCLIORI_0 = " + company + ".BPARTNER.BPRNUM_0 "
				+ "WHERE UPPER("
				+ company + ".MFGITM.MFGNUM_0) = ? ",
        new Object[]{number.toUpperCase()});

		X3ProductionOrderDetails order = null;
		
		if(resultSet.isEmpty()) {
			resultSet = jdbc.queryForList(
					"SELECT " 
					+ company + ".MFGITM.MFGNUM_0, "
					+ company + ".MFGITM.ITMREF_0, "
					+ company + ".MFGITM.EXTQTY_0, "
					+ company + ".ITMMASTER.ITMDES1_0, "
					+ company + ".ITMMASTER.ITMDES2_0, "
					+ company + ".SORDER.SOHNUM_0, "
					+ company + ".SORDER.XCLIORI_0, "
					+ company + ".BPARTNER.BPRNAM_0, "
					+ company + ".BPARTNER.BPRNAM_1 "
					+ "FROM (( " 
					+ company + ".MFGITM INNER JOIN " + company + ".ITMMASTER "
					+ "ON " 
					+ company + ".MFGITM.ITMREF_0 = " + company +  ".ITMMASTER.ITMREF_0 ) "
					+ "INNER JOIN "
					+ company + ".SORDER "
					+ "ON "
					+ company + ".SORDER.SOHNUM_0 = "
					+ "SUBSTR(" + company +  ".MFGITM.PJT_0, 0, LENGTH(" + company +  ".MFGITM.PJT_0) - 2)" 
					+ ") "
					+ "INNER JOIN "
					+ company + ".BPARTNER ON "
					+ company + ".SORDER.XCLIORI_0 = " + company + ".BPARTNER.BPRNUM_0 "
					+ "WHERE UPPER("
					+ company + ".MFGITM.MFGNUM_0) = ? ",
	        new Object[]{number.toUpperCase()});
		}
	
		for(Map<String,Object> row: resultSet ){
			order = new X3ProductionOrderDetails();
			order.setProductionOrderNumber((String)row.get("MFGNUM_0"));
			order.setClientCode((String)row.get("XCLIORI_0"));
			order.setClientName(((String)row.get("BPRNAM_0")) + ((String)row.get("BPRNAM_1")));
			order.setProductCode((String)row.get("ITMREF_0"));
			order.setProductDescription(((String)row.get("ITMDES1_0")) + ((String)row.get("ITMDES2_0")));
			order.setSalesOrderNumber((String)row.get("SOHNUM_0"));
			order.setProducedQuantity(((BigDecimal)row.get("EXTQTY_0")).intValue());
		}

		return order;
	}

	@Override
	public List<X3BomItem> findBomPartsByParent(String company, String productCode) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		Timestamp now = dateHelper.getCurrentTime();
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
			    + company + ".BOMD.BOMSEQ_0, "
				+ company + ".BOMD.CPNITMREF_0, "
				+ company + ".BOMD.LIKQTY_0, "
				+ company + ".BOMD.BOMUOM_0, "
				+ company + ".BOMD.BOMSTRDAT_0, "
				+ company + ".BOMD.BOMENDDAT_0, "
				+ company + ".ITMMASTER.ITMDES1_0, "
				+ company + ".ITMMASTER.ITMDES2_0 "
				+ "FROM "
				+ company + ".ITMMASTER INNER JOIN " + company + ".BOMD "
				+ "ON "
				+ company + ".ITMMASTER.ITMREF_0 = " + company + ".BOMD.CPNITMREF_0 "
				+ "WHERE UPPER("
				+ company + ".BOMD.ITMREF_0) = ? "
				+ "AND "
				+ company + ".BOMD.BOMALT_0 = 1 "
				+ "AND ("
					+ company + ".BOMD.BOMSTRDAT_0 < ? "
					+ "OR "
					+ company + ".BOMD.BOMSTRDAT_0 = TO_TIMESTAMP('1599/12/31 00:00:00')"
				+ ")"
				+ "AND ("
					+ company + ".BOMD.BOMENDDAT_0 > ? "
					+ "OR "
					+ company + ".BOMD.BOMENDDAT_0 = TO_TIMESTAMP('1599/12/31 00:00:00')"
				+ ") "
				+ "ORDER BY "
				+ company + ".BOMD.BOMSEQ_0",
                new Object[]{productCode.toUpperCase(), now, now});
        
		List<X3BomItem> result = new ArrayList<>();
		X3BomItem item = null;
		
        for(Map<String,Object> row: resultSet ){
        	item = new X3BomItem();
        	item.setSequence(((BigDecimal)row.get("BOMSEQ_0")).intValue());
        	item.setPartCode((String)row.get("CPNITMREF_0"));
        	item.setPartDescription(((String)row.get("ITMDES1_0")) + " " +  ((String)row.get("ITMDES2_0")));
        	item.setModelUnit((String)row.get("BOMUOM_0"));
        	item.setModelQuantity(((BigDecimal)row.get("LIKQTY_0")).doubleValue());
        	result.add(item);
        }
		
		return result;
	}

	@Override
	public List<WpslookRow> findLocationsOfProduct(String company, String code) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		try {
			String query = "SELECT " + company + ".STOCK.STOCOU_0, " + company + ".STOCK.ITMREF_0, " + company
					+ ".STOCK.LOC_0, " + company + ".STOCK.QTYSTU_0, " + company + ".STOCK.PCU_0, " + company
					+ ".STOCK.LASRCPDAT_0, " + company + ".STOCK.LASISSDAT_0 " + "FROM " + company + ".STOCK "
					+ "WHERE " + company + ".STOCK.ITMREF_0 = ? ";

			List<WpslookRow> result = new ArrayList<>();
			WpslookRow item = null;
			Timestamp tmp, date1900;
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date;

			date = dateFormat.parse("01/01/1900");
			long time = date.getTime();
			date1900 = new Timestamp(time);

			for (Map<String, Object> row : jdbc.queryForList(query, new Object[] { code.toUpperCase() })) {
				item = new WpslookRow();
				item.setChronoNumber((BigDecimal) row.get("STOCOU_0"));
				item.setProduct((String) row.get("ITMREF_0"));
				item.setAddress((String) row.get("LOC_0"));
				item.setQuantity((BigDecimal) row.get("QTYSTU_0"));
				item.setUnit((String) row.get("PCU_0"));
				tmp = (Timestamp) row.get("LASRCPDAT_0");
				if(tmp.before(date1900)){
					item.setLastInputDate(null);
				}
				else{
					item.setLastInputDate(tmp);
				}
				tmp = (Timestamp) row.get("LASISSDAT_0");
				if(tmp.before(date1900)){
					item.setLastOutputDate(null);
				}
				else{
					item.setLastOutputDate(tmp);
				}
				result.add(item);
			}
			return result;
		} catch (ParseException e) {
			return null;
		}
	}

	@Override
	public List<X3BomItem> findProductionPartsByProductionOrderAndOperation(String company, String productionOrder,
			int operationNumber) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ company + ".MFGMAT.BOMSEQ_0, "
				+ company + ".MFGMAT.ITMREF_0, "
				+ company + ".ITMMASTER.ITMDES1_0, "
				+ company + ".ITMMASTER.ITMDES2_0, "
				+ company + ".MFGMAT.LIKQTY_0, "
				+ company + ".MFGMAT.STU_0 "
				+ "FROM "
				+ company + ".MFGMAT INNER JOIN " + company + ".ITMMASTER "
				+ "ON "
				+ company + ".MFGMAT.ITMREF_0 = " + company + ".ITMMASTER.ITMREF_0 "
				+ "WHERE "
				+ company + ".MFGMAT.MFGNUM_0 = ? "
				+ "AND "
				+ company + ".MFGMAT.BOMOPE_0 = ? "
				+ "ORDER BY "
				+ company + ".MFGMAT.ITMREF_0 ASC",
                new Object[]{productionOrder.toUpperCase(), operationNumber});
        
		List<X3BomItem> result = new ArrayList<>();
		X3BomItem item = null;
		
        for(Map<String,Object> row: resultSet ){
        	item = new X3BomItem();
        	item.setSequence(((BigDecimal)row.get("BOMSEQ_0")).intValue());
        	item.setPartCode((String)row.get("ITMREF_0"));
        	item.setPartDescription(((String)row.get("ITMDES1_0")) + " " +  ((String)row.get("ITMDES2_0")));
        	item.setModelUnit((String)row.get("STU_0"));
        	item.setModelQuantity(((BigDecimal)row.get("LIKQTY_0")).doubleValue());
        	result.add(item);
        }
		
		return result;
	}

	@Override
	public String findOperationDescriptionByProductionOrder(String company, String productionOrder,
			int operationNumber) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ company + ".MFGOPE.ROODES_0 "
				+ "FROM "
				+ company + ".MFGOPE "
				+ "WHERE "
				+ company + ".MFGOPE.MFGNUM_0 = ? "
				+ "AND "
				+ company + ".MFGOPE.OPENUM_0 = ?"		
				,
                new Object[]{productionOrder.toUpperCase(), operationNumber});
        
		String description = null;
        for(Map<String,Object> row: resultSet ){
        	description = ((String)row.get("ROODES_0"));
        }
		return description;
	}

	@Override
	public String findFinalClientByOrder(String company, String order) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ company + ".SORDER.XCLIORI_0 "
				+ "FROM "
				+ company + ".SORDER "
				+ "WHERE UPPER(" + company + ".SORDER.SOHNUM_0) = ? "
				,
                new Object[]{order});
        
        String result = null;
        for(Map<String,Object> row: resultSet ){
        	result = (String)row.get("XCLIORI_0");
        }
		return result;
	}

	@Override
	public Map<String, X3UtrMachine> findAllUtrMachines(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		/*
		String sqlV6 = "SELECT "
				+ "ym.YCESPITE_0, "
				+ "ym.YCESPDESCR_0, "
				+ "ym.YFLAG_0, "
				+ "ym.YSOCE_0, "
				+ "fx.NICCOD_0 "
				+ "FROM "
				+ company + ".YMANUPREVE ym LEFT JOIN "
				+ "(SELECT "
				+ company + ".FXDASSETS.AASREF_0, "
				+ company + ".FXDASSETS.AASDES2_0, "
				+ company + ".VFXDASSETINF.NICCOD_0 "
				+ "FROM "
				+ company + ".FXDASSETS LEFT JOIN "
				+ company + ".VFXDASSETINF "
				+ "ON "
				+ company + ".FXDASSETS.AASREF_0 = " + company + ".VFXDASSETINF.AASREF_0) fx "
				+ "ON ym.YCESPITE_0 = fx.AASDES2_0";
 		*/ 		
		String sqlV11 = "" 
				+ "SELECT "
					+ "ym.YCESPITE_0, "
					+ "ym.YCESPDESCR_0, "
					+ "ym.YFLAG_0, "
					+ "ym.YSOCE_0, "
					+ "fx.AASREF_0, "
					+ "fx.AASDES2_0 "
				+ "FROM "
					+ company + ".YMANUPREVE ym LEFT JOIN " + company + ".FXDASSETS fx "
				+ "ON "
					+ "ym.YCESPITE_0 = fx.AASDES2_0";
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(sqlV11,new Object[]{});
        
        Map <String, X3UtrMachine> map = new HashMap<>();
        X3UtrMachine machine;
        String nicimCode;
        for(Map<String,Object> row: resultSet ){
        	machine = new X3UtrMachine();
        	machine.setCode((String)row.get("YCESPITE_0"));
        	machine.setName((String)row.get("YCESPDESCR_0"));
        	if(machine.getName().split(" ").length > 0) {
        		nicimCode = machine.getName().split(" ")[0];
        	}
        	else {
        		nicimCode = "N/D";
        	}
        	machine.setCodeNicim(nicimCode);
        	machine.setCompany(((BigDecimal)row.get("YSOCE_0")).intValue());
        	if(((BigDecimal)row.get("YFLAG_0")).intValue() == 2){
        		machine.setCritical(true);
        	}
        	else{
        		machine.setCritical(false);
        	}
        	map.put(machine.getCode(), machine);
        }
		return map;
	}

	@Override
	public Map<String, X3UtrWorker> findAllUtrWorkers(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				  
				"SELECT "
				+ company + ".ATEXTRA.IDENT2_0, "
				+ company + ".ATEXTRA.TEXTE_0 "
				+ "FROM "
				+ company + ".ATEXTRA "
				+ "WHERE " 
				+ company + ".ATEXTRA.CODFIC_0 = ? AND "
				+ company + ".ATEXTRA.ZONE_0 = ? AND "
				+ company + ".ATEXTRA.LANGUE_0 = ? AND "
				+ company + ".ATEXTRA.IDENT1_0 = ? "
				,
                new Object[]{"ATABDIV","LNGDES","POL","6001"});
        Map <String, X3UtrWorker> map = new HashMap<>();
        X3UtrWorker worker;
        for(Map<String,Object> row: resultSet ){
        	worker = new X3UtrWorker();
        	worker.setCode((String)row.get("IDENT2_0"));
        	worker.setName((String)row.get("TEXTE_0"));
        	map.put(worker.getCode(), worker);
        }
		return map;
	}

	@Override
	public List<X3ShipmentMovement> findAdrShipmentMovementsInPeriod(Date startDate, Date endDate) {
		String company = "ATW";
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
				
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				
				"SELECT "
				+ company + ".YCHGSTKGX.CREDAT_0, "
				+ company + ".YCHGSTKGX.VCRNUM_0, "
				+ company + ".YCHGSTKGX.CRETIM_0, "
				+ company + ".YCHGSTKGX.ITMREF_0, "
				+ company + ".ITMMASTER.ITMDES1_0, "
				+ company + ".ITMMASTER.TCLCOD_0, "
				+ company + ".ITMMASTER.TSICOD_1, "
				+ company + ".YCHGSTKGX.QTYSTU_0, "
				+ company + ".YCHGSTKGX.YWO_0, "
				+ company + ".ITMMVT.AVC_0, "
				+ "ORD.BPCORD_0, "
				+ "ORD.BPCNAM_0 "
				+ "FROM "
				+ "( "	
				+ "SELECT " 
				+ company + ".SORDER.SOHNUM_0, "
				+ company + ".SORDER.BPCORD_0, "
				+ company + ".BPCUSTOMER.BPCNAM_0, "
				+ company + ".SORDER.ORDDAT_0, "
				+ company + ".SORDER.ORDSTA_0 "
				+ "FROM " 
				+ company + ".BPCUSTOMER INNER JOIN " + company + ".SORDER "
				+ "ON "
				+ company + ".BPCUSTOMER.BPCNUM_0 = " + company + ".SORDER.BPCORD_0 "
				+ ") ORD "
		+ "RIGHT JOIN ("
				+ company + ".ITMMVT "
				+ "INNER JOIN (" + company + ".YCHGSTKGX INNER JOIN " + company + ".ITMMASTER "
				+ "ON "
				+ company + ".YCHGSTKGX.ITMREF_0 = " + company + ".ITMMASTER.ITMREF_0) "
				+ "ON "
				+ company + ".ITMMVT.ITMREF_0 = " + company + ".YCHGSTKGX.ITMREF_0 "
		+ ") ON ORD.SOHNUM_0 = " + company + ".YCHGSTKGX.YWO_0 "
				+ "WHERE "
				+ company + ".YCHGSTKGX.CREDAT_0 >= ? "
				+ "AND "
				+ company + ".YCHGSTKGX.CREDAT_0 <= ? "
				+ "AND "
				+ company + ".YCHGSTKGX.LOCDES_0 = ? "
				+ "AND ("
				+ company + ".ITMMASTER.TCLCOD_0 = ? "
				+ "OR "
				+ company + ".ITMMASTER.TCLCOD_0 = ? "
				+ ") "
				+ "AND "
				+ company + ".YCHGSTKGX.YFLGTRS_0 = 1 "
				+ "AND "
				+ company + ".YCHGSTKGX.YVCRTYP_0 = 2 "
				+ "ORDER BY " 
				+ company + ".YCHGSTKGX.CREDAT_0, " 
				+ company + ".YCHGSTKGX.ITMREF_0",
                new Object[]{
                		dateHelper.getTime(startDate), 
                		dateHelper.getTime(endDate), 
                		"WGX01", 
                		"AFV", "ACV"
                		}
				);
        
		List<X3ShipmentMovement> result = new ArrayList<>();
		X3ShipmentMovement item = null;
		Calendar cal;
		
        for(Map<String,Object> row: resultSet ){
        	item = new X3ShipmentMovement();
        	item.setMovementNumber((String)row.get("VCRNUM_0"));
        	item.setItemCode((String)row.get("ITMREF_0"));
        	item.setItemDescription(((String)row.get("ITMDES1_0")));
        	item.setItemCategory(((String)row.get("TCLCOD_0")));
        	item.setGr2(((String)row.get("TSICOD_1")));
        	item.setQuantity(((BigDecimal)row.get("QTYSTU_0")).doubleValue());
        	item.setEmergencyAveragePrice(((BigDecimal)row.get("AVC_0")).doubleValue());
        	item.setDocument(((String)row.get("YWO_0")).trim());
        	item.setClientCode((String)row.get("BPCORD_0"));
        	item.setClientName((String)row.get("BPCNAM_0"));
        	
        	// set date/time
        	try{
	        	cal = Calendar.getInstance();
	        	cal.setTime((Timestamp)row.get("CREDAT_0"));
	        	String[] timeTab = ((String)row.get("CRETIM_0")).split(":");
	        	cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(timeTab[0]));
	        	cal.add(Calendar.MINUTE, Integer.parseInt(timeTab[1]));
	        	cal.add(Calendar.SECOND, Integer.parseInt(timeTab[2]));
	        	item.setDate(new Timestamp(cal.getTime().getTime()));
        	}
        	catch (Exception e){
        		item.setDate((Timestamp)row.get("CREDAT_0"));
        	}
        	
        	result.add(item);
        }
		
		return result;
	}
	
	@Override
	public List<X3SalesOrderLine> findAdrSalesOrderLinesBasedOnShipmentMovementsInPeriod(Date startDate, Date endDate) {
		String company = "ATW";
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "SOQ.SOHNUM_0, "
				+ "SOQ.SOPLIN_0, "
				+ "SOQ.QTYSTU_0, "
				+ "(SOQ.QTYSTU_0 - SOQ.ODLQTYSTU_0 - SOQ.DLVQTY_0) AS LEFT_TO_SEND,  "
				+ "SOQ.DEMDLVDAT_0, "
				+ "SOP.X_DATAORI_0, "
				+ "SOQ.CREDAT_0, "
				+ "SOQ.UPDDAT_0, "
				+ "SOQ.DEMSTA_0, "
				+ "SOP.NETPRI_0, "
				+ "SOR.CUR_0, "
				+ "SOR.CHGRAT_0, "
				+ "ITM.ITMREF_0, "
				+ "ITM.ITMDES1_0, "
				+ "ITM.TSICOD_0, "
				+ "ITM.TSICOD_1, "
				+ "ITM.TSICOD_2, "
				+ "BPR.BPRNUM_0, "
				+ "BPR.BPRNAM_0, "
				+ "BPR.CRY_0,"
				+ "SOR.XCLIORI_0,"
				+ "BPR2.BPRNAM_0 AS BPRNAM2 "
				
				+ "FROM ("
				+ "("
				+ "("
				+ "("
				+ "(" + company + ".SORDERQ SOQ INNER JOIN " + company + ".SORDERP SOP "
				+ "ON SOQ.SOHNUM_0 = SOP.SOHNUM_0 AND "
				+ "SOQ.SOPLIN_0 = SOP.SOPLIN_0 AND "
				+ "SOQ.SOQSEQ_0 = SOP.SOPSEQ_0 "
				+ ") "
				+ "INNER JOIN " + company + ".ITMMASTER ITM "
				+ "ON SOQ.ITMREF_0 = ITM.ITMREF_0) "
				+ "INNER JOIN " + company + ".SORDER SOR ON SOQ.SOHNUM_0 = SOR.SOHNUM_0) "
				+ "INNER JOIN " + company + ".BPARTNER BPR ON SOR.X_CODCLI_0 = BPR.BPRNUM_0) "
				+ "INNER JOIN " + company + ".YCHGSTKGX YCHGX "
				+ "ON SOR.SOHNUM_0 = YCHGX.YWO_0 "
				+ "AND ITM.ITMREF_0 = YCHGX.ITMREF_0)"
				+ "INNER JOIN " + company + ".BPARTNER BPR2 ON SOR.XCLIORI_0 = BPR2.BPRNUM_0 "
				+ "WHERE "
				+ "YCHGX.CREDAT_0 >= ? "
				+ "AND "
				+ "YCHGX.CREDAT_0 <= ? "
				+ "AND "
				+ "YCHGX.LOCDES_0 = ? "
				+ "AND ("
				+ "ITM.TCLCOD_0 = ? "
				+ "OR "
				+ "ITM.TCLCOD_0 = ? "
				+ ") "
				+ "AND "
				+ "YCHGX.YFLGTRS_0 = 1 "
				+ "AND "
				+ "YCHGX.YVCRTYP_0 = 2 "
				+ "ORDER BY SOQ.CREDAT_0 DESC"

				,
                new Object[]{
                		dateHelper.getTime(startDate), 
						dateHelper.getTime(endDate), 
						"WGX01", 
						"AFV", "ACV"}
				);
		
		
		List<X3SalesOrderLine> list = new ArrayList<>();
		X3SalesOrderLine line;
		for(Map<String,Object> row: resultSet ){
			line = new X3SalesOrderLine();
			line.setOrderNumber((String)row.get("SOHNUM_0"));
			line.setOrderLineNumber(((BigDecimal)row.get("SOPLIN_0")).intValue());
			line.setProductCode((String)row.get("ITMREF_0"));
			line.setProductDescription((String)row.get("ITMDES1_0"));
			line.setProductGr1((String)row.get("TSICOD_0"));
			line.setProductGr2((String)row.get("TSICOD_1"));
			line.setProductGr3((String)row.get("TSICOD_2"));
			line.setClientCode((String)row.get("BPRNUM_0"));
			line.setClientName(((String)row.get("BPRNAM_0")));
			line.setCountry(((String)row.get("CRY_0")));
			line.setDemandedDate(((Timestamp)row.get("DEMDLVDAT_0")));
			line.setOriginalDate(((Timestamp)row.get("X_DATAORI_0")));
			line.setCreationDate(((Timestamp)row.get("CREDAT_0")));
			line.setUpdateDate(((Timestamp)row.get("UPDDAT_0")));
			line.setQuantityLeftToSend(((BigDecimal)row.get("LEFT_TO_SEND")).intValue());
			line.setQuantityOrdered(((BigDecimal)row.get("QTYSTU_0")).intValue());
			line.setUnitPrice(((BigDecimal)row.get("NETPRI_0")).doubleValue());
			line.setExchangeRate(((BigDecimal)row.get("CHGRAT_0")).doubleValue());
			line.setCurrency((String)row.get("CUR_0"));
			line.setDemandState(((BigDecimal)row.get("DEMSTA_0")).intValue());
			line.setFinalClientCode((String)row.get("XCLIORI_0"));
			line.setFinalClientName((String)row.get("BPRNAM2"));
			list.add(line);
		}

		
		return list;
	}

	@Override
	public Map<String, X3UtrFault> findUtrFaultsInPeriod(Date startDate, Date endDate) {
		String company = "ATW";
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ company + ".XMANUTGUA.XNUMMOD_0, "
				+ company + ".XMANUTGUA.XPROP_0, "
				+ company + ".AUTILIS.NOMUSR_0, "
				+ company + ".XMANUTGUA.XDATACRE_0, "
				+ company + ".XMANUTGUA.XCESPITE_0, "
				+ company + ".XMANUTGUA.YTBLOCCO_0, "
				+ company + ".XMANUTGUA.YUBICA_0 "
				+ "FROM "
				+ company + ".XMANUTGUA INNER JOIN " + company + ".AUTILIS "
				+ "ON "
				+ company + ".XMANUTGUA.XPROP_0 = " + company + ".AUTILIS.USR_0 "
				+ "WHERE "
				+ company + ".XMANUTGUA.XDATACRE_0 >= ? "
				+ "AND "
				+ company + ".XMANUTGUA.XDATACRE_0 <= ?",
                new Object[]{
                		dateHelper.getTime(startDate), 
                		dateHelper.getTime(endDate)
                		}
				);
        
		Map <String, X3UtrFault> map = new HashMap<>();
		X3UtrFault fault;
		
        for(Map<String,Object> row: resultSet ){
        	fault = new X3UtrFault();
        	fault.setFaultNumber((String)row.get("XNUMMOD_0"));
        	fault.setCreatorCode((String)row.get("XPROP_0"));
        	fault.setCreatorName((String)row.get("NOMUSR_0"));
        	fault.setCreationDate((Timestamp)row.get("XDATACRE_0"));
        	fault.setMachineCode((String)row.get("XCESPITE_0"));
        	fault.setLocationName((String)row.get("YUBICA_0"));
        	if(((BigDecimal)row.get("YTBLOCCO_0")).intValue() == 2){
        		fault.setFaultType(X3UtrFault.STOP_TYPE);
        		
        	}
        	else{
        		fault.setFaultType(X3UtrFault.NOSTOP_TYPE);
        	}
        	map.put(fault.getFaultNumber(), fault);
        }
		return map;
	}
	
	@Override
	public Map<String, X3UtrFault> findAllUtrFaults() {
		String company = "ATW";
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ company + ".XMANUTGUA.XNUMMOD_0, "
				+ company + ".XMANUTGUA.XPROP_0, "
				+ company + ".AUTILIS.NOMUSR_0, "
				+ company + ".XMANUTGUA.XDATACRE_0, "
				+ company + ".XMANUTGUA.XCESPITE_0, "
				+ company + ".XMANUTGUA.YTBLOCCO_0, "
				+ company + ".XMANUTGUA.YUBICA_0 "
				+ "FROM "
				+ company + ".XMANUTGUA INNER JOIN " + company + ".AUTILIS "
				+ "ON "
				+ company + ".XMANUTGUA.XPROP_0 = " + company + ".AUTILIS.USR_0 ",
                new Object[]{}
				);
        
		Map <String, X3UtrFault> map = new HashMap<>();
		X3UtrFault fault;
		
        for(Map<String,Object> row: resultSet ){
        	fault = new X3UtrFault();
        	fault.setFaultNumber((String)row.get("XNUMMOD_0"));
        	fault.setCreatorCode((String)row.get("XPROP_0"));
        	fault.setCreatorName((String)row.get("NOMUSR_0"));
        	fault.setCreationDate((Timestamp)row.get("XDATACRE_0"));
        	fault.setMachineCode((String)row.get("XCESPITE_0"));
        	fault.setLocationName((String)row.get("YUBICA_0"));
        	if(((BigDecimal)row.get("YTBLOCCO_0")).intValue() == 2){
        		fault.setFaultType(X3UtrFault.STOP_TYPE);
        		
        	}
        	else{
        		fault.setFaultType(X3UtrFault.NOSTOP_TYPE);
        	}
        	map.put(fault.getFaultNumber(), fault);
        }
		return map;
	}
	
	@Override
	public X3UtrFault findUtrFault(String number) {
		String company = "ATW";
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
				
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ company + ".XMANUTGUA.XNUMMOD_0, "
				+ company + ".XMANUTGUA.XPROP_0, "
				+ company + ".XMANUTGUA.XTIPO_0, "
				+ company + ".AUTILIS.NOMUSR_0, "
				+ company + ".XMANUTGUA.XDATACRE_0, "
				+ company + ".XMANUTGUA.XCESPITE_0, "
				+ company + ".XMANUTGUA.YDESCCES_0, "
				+ company + ".XMANUTGUA.YTBLOCCO_0, "
				+ company + ".XMANUTGUA.YUBICA_0 "
				+ "FROM "
				+ company + ".XMANUTGUA INNER JOIN " + company + ".AUTILIS "
				+ "ON "
				+ company + ".XMANUTGUA.XPROP_0 = " + company + ".AUTILIS.USR_0 "
				+ "WHERE " + company + ".XMANUTGUA.XNUMMOD_0 = ?",
                new Object[]{number}
				);
        
		X3UtrFault fault = null;
        for(Map<String,Object> row: resultSet ){
        	fault = new X3UtrFault();
        	fault.setFaultNumber((String)row.get("XNUMMOD_0"));
        	fault.setCreatorCode((String)row.get("XPROP_0"));
        	fault.setCreatorName((String)row.get("NOMUSR_0"));
        	fault.setCreationDate((Timestamp)row.get("XDATACRE_0"));
        	fault.setMachineCode((String)row.get("XCESPITE_0"));
        	fault.setMachineName((String)row.get("YDESCCES_0"));
        	fault.setLocationName((String)row.get("YUBICA_0"));
        	if(((BigDecimal)row.get("YTBLOCCO_0")).intValue() == 2){
        		fault.setFaultType(X3UtrFault.STOP_TYPE);
        	}
        	else{
        		fault.setFaultType(X3UtrFault.NOSTOP_TYPE);
        	}
        
        	switch (((BigDecimal)row.get("XTIPO_0")).intValue()){
        		case 1:
        			fault.setFaultKind(X3UtrFault.KIND_ELECTRIC);
        			break;
        		case 2:
        			fault.setFaultKind(X3UtrFault.KIND_MECHANICAL);
        			break;
        		case 3:
        			fault.setFaultKind(X3UtrFault.KIND_HYDRAULIC);
        			break;
        		default:
        				
        	}
        	
        }
		return fault;
	}


	@Override
	public List<X3UtrFaultLine> findUtrFaultLinesAfterDate(Date startDate) {
		String company = "ATW";
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
				
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ company + ".XMANSTGUA.XNUMMOD_0, "
				+ company + ".XMANSTGUA.XMANUTENTO_0, "
				+ company + ".XMANSTGUA.XDATAIN_0, "
				+ company + ".XMANSTGUA.XORAIN_0, "
				+ company + ".XMANSTGUA.XDATAFIN_0, "
				+ company + ".XMANSTGUA.XORAFIN_0, "
				+ company + ".XMANSTGUA.XSTATO_0 "
				+ "FROM "
				+ company + ".XMANSTGUA "
				+ "WHERE "
				+ company + ".XMANSTGUA.XDATAIN_0 > ?"
				,
                new Object[]{dateHelper.getTime(startDate)}
				);
        
		List<X3UtrFaultLine> result = new ArrayList<>();
		X3UtrFaultLine item = null;
		

        for(Map<String,Object> row: resultSet ){
        	item = new X3UtrFaultLine();
        	item.setFaultNumber((String)row.get("XNUMMOD_0"));
        	item.setUtrWorkerCode((String)row.get("XMANUTENTO_0"));
        	item.setState(((BigDecimal)row.get("XSTATO_0")).intValue());
        	item.setStartDateTime(x3UtrFaultLineDateConvert((Timestamp) row.get("XDATAIN_0"),(String)row.get("XORAIN_0")));
        	item.setEndDateTime(x3UtrFaultLineDateConvert((Timestamp) row.get("XDATAFIN_0"),(String)row.get("XORAFIN_0")));
        	
        	result.add(item);
        }
		return result;
	}
	
	@Override
	public List<X3UtrFaultLine> findAllUtrFaultLines() {
		String company = "ATW";
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
				
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ company + ".XMANSTGUA.XNUMMOD_0, "
				+ company + ".XMANSTGUA.XMANUTENTO_0, "
				+ company + ".XMANSTGUA.XDATAIN_0, "
				+ company + ".XMANSTGUA.XORAIN_0, "
				+ company + ".XMANSTGUA.XDATAFIN_0, "
				+ company + ".XMANSTGUA.XORAFIN_0, "
				+ company + ".XMANSTGUA.XSTATO_0 "
				+ "FROM "
				+ company + ".XMANSTGUA "
				,
                new Object[]{}
				);
        
		List<X3UtrFaultLine> result = new ArrayList<>();
		X3UtrFaultLine item = null;
		

        for(Map<String,Object> row: resultSet ){
        	item = new X3UtrFaultLine();
        	item.setFaultNumber((String)row.get("XNUMMOD_0"));
        	item.setUtrWorkerCode((String)row.get("XMANUTENTO_0"));
        	item.setState(((BigDecimal)row.get("XSTATO_0")).intValue());
        	item.setStartDateTime(x3UtrFaultLineDateConvert((Timestamp) row.get("XDATAIN_0"),(String)row.get("XORAIN_0")));
        	item.setEndDateTime(x3UtrFaultLineDateConvert((Timestamp) row.get("XDATAFIN_0"),(String)row.get("XORAFIN_0")));
        	
        	result.add(item);
        }
		return result;
	}
		
	private Timestamp x3UtrFaultLineDateConvert(Timestamp date, String hour) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Timestamp date1900 = new Timestamp(dateFormat.parse("01/01/1900").getTime());
			if (date.before(date1900) || hour.length() != 4) {
				return null;
			} else {
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(date.getTime());
				cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour.substring(0, 2)));
				cal.set(Calendar.MINUTE, Integer.parseInt(hour.substring(2, 4)));
				return new Timestamp(cal.getTimeInMillis());
			}
		} catch (ParseException e) {
			return null;
		}
	}

	@Override
	public Map<String, X3ProductFinalMachine> findX3ProductFinalMachines(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
				
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ company + ".ROUOPE.ITMREF_0 AS ITM, "
				+ company + ".ROUOPE.OPENUM_0 AS OPE, "
				+ company + ".ROUOPE.WST_0 AS WST, "
				+ company + ".WORKSTATIO.WSTDES_0 AS WSTDES, "
				+ company + ".WORKSTATIO.WCR_0 AS WCR "
				+ "FROM "
				+ company + ".ROUOPE INNER JOIN " + company + ".WORKSTATIO "
				+ "ON "
				+ company + ".ROUOPE.WST_0 = "+ company + ".WORKSTATIO.WST_0 "
				+ "ORDER BY ITM ASC, OPE ASC"
				,
                new Object[]{}
				);
        
		Map<String, X3ProductFinalMachine> result = new HashMap<>();
		X3ProductFinalMachine item = null;
		
        for(Map<String,Object> row: resultSet ){
        	item = new X3ProductFinalMachine();
        	item.setProductCode((String)row.get("ITM"));
        	item.setMachineCode((String)row.get("WST"));
        	item.setMachineName((String)row.get("WSTDES"));
        	item.setMachineGroup((String)row.get("WCR"));
        	item.setOperation(((BigDecimal)row.get("OPE")).intValue());
        	result.put(item.getProductCode(), item);
        }
        
		return result;
	}

	@Override
	public List<Project> findPendingProjectsProgress() {
		String company = "ATW";
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				// main project info
				+ "CAC.YCODPROG_0, " + "CAC.YDESCPROG_0, " + "CAC.YDTPROG_0, " + "CAC.YORAPROG_0, "
				// client accept
				+ "CAC.YACCCLI_0, " + "CAC.YDTACCCLI_0, " + "CAC.YORAACCCLI_0, "
				// codification 
				+ "CAC.YCODADR_0, " + "CAC.YDTCODADR_0, " + "CAC.YORACODADR_0, "
				// drawing verified
				+ "CAC.YDISVER_0, " + "CAC.YCODICE_0, " + "CAC.YDTUFFTEC_0, " + "CAC.YORAUFFTEC_0, "
				// sales order
				+ "CAC.YORDINE_0, " + "CAC.YPEZZIORD_0, " + "CAC.YDTORDINE_0, " + "CAC.YORAORDINE_0, "
				// purchase plan
				+ "CAC.YPREVISTO_0, " + "CAC.YDTPREVISTO_0, " + "CAC.YORAPREVISTO_0, "
				// purchase
				+ "CAC.YACQNEW_0, " + "CAC.YDTACQNEW_0, " + "CAC.YORAACQNEW_0, "
				// tool drawing
				+ "CAC.YATTREZ_0, " + "CAC.YDTATTREZ_0, " + "CAC.YORAATTREZ_0, "
				// tool creation
				+ "CAC.YATTROK_0, " + "CAC.YDTATTROK_0, " + "CAC.YORAATTROK_0, "
				// technology
				+ "CAC.YTECNOL_0, " + "CAC.YDTTECNOL_0, " + "CAC.YORATECNOL_0, "
				// quality
				+ "CAC.YQUALIT_0, " + "CAC.YDTQUALIT_0, " + "CAC.YORAQUALIT_0, "
				// general project state
				+ "CAC.YSTATOCA_0, "
				// additional description (SIC)
				+ "CAC.YDESCRIZIONE_0 "
				// table
				+ "FROM " + company + ".YCACANA CAC "
				+ "WHERE CAC.YSTATOCA_0 < ?"
				,
                new Object[]{Project.STATE_FINISHED}
				);
        
		List<Project> result = new ArrayList<>();

        for(Map<String,Object> row: resultSet ){
        	result.add(mapRowToProject(row));
        }
		return result;

	}

	@Override
	public Project findProjectProgressByNumber(String number) {
		String company = "ATW";
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
				
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				// main project info
				+ "CAC.YCODPROG_0, " + "CAC.YDESCPROG_0, " + "CAC.YDTPROG_0, " + "CAC.YORAPROG_0, "
				// client accept
				+ "CAC.YACCCLI_0, " + "CAC.YDTACCCLI_0, " + "CAC.YORAACCCLI_0, "
				// codification 
				+ "CAC.YCODADR_0, " + "CAC.YDTCODADR_0, " + "CAC.YORACODADR_0, "
				// drawing verified
				+ "CAC.YDISVER_0, " + "CAC.YCODICE_0, " + "CAC.YDTUFFTEC_0, " + "CAC.YORAUFFTEC_0, "
				// sales order
				+ "CAC.YORDINE_0, " + "CAC.YPEZZIORD_0, " + "CAC.YDTORDINE_0, " + "CAC.YORAORDINE_0, "
				// purchase plan
				+ "CAC.YPREVISTO_0, " + "CAC.YDTPREVISTO_0, " + "CAC.YORAPREVISTO_0, "
				// purchase
				+ "CAC.YACQNEW_0, " + "CAC.YDTACQNEW_0, " + "CAC.YORAACQNEW_0, "
				// tool drawing
				+ "CAC.YATTREZ_0, " + "CAC.YDTATTREZ_0, " + "CAC.YORAATTREZ_0, "
				// tool creation
				+ "CAC.YATTROK_0, " + "CAC.YDTATTROK_0, " + "CAC.YORAATTROK_0, "
				// technology
				+ "CAC.YTECNOL_0, " + "CAC.YDTTECNOL_0, " + "CAC.YORATECNOL_0, "
				// quality
				+ "CAC.YQUALIT_0, " + "CAC.YDTQUALIT_0, " + "CAC.YORAQUALIT_0, "
				// general project state
				+ "CAC.YSTATOCA_0, "
				// additional description (SIC)
				+ "CAC.YDESCRIZIONE_0 "
				// table
				+ "FROM " + company + ".YCACANA CAC "
				+ "WHERE CAC.YCODPROG_0 = ?"
				,
                new Object[]{number}
				);
        
		Project result = new Project();
        for(Map<String,Object> row: resultSet ){
        	result = mapRowToProject(row);
        }
		return result;
	}
	
	private Project mapRowToProject(Map<String,Object> row){
		boolean tmpBoolean;
		
		Project item = new Project();
		item.setProjectNumber((String)row.get("YCODPROG_0"));
    	item.setProjectDescription((String)row.get("YDESCPROG_0"));
    	item.setProjectCreationDate(x3UtrFaultLineDateConvert((Timestamp) row.get("YDTPROG_0"),(String)row.get("YORAPROG_0")));
    	tmpBoolean = ((BigDecimal)row.get("YACCCLI_0")).intValue() == 2 ? true : false;
    	item.setClientAccept(tmpBoolean);
    	item.setClientAcceptDate(x3UtrFaultLineDateConvert((Timestamp) row.get("YDTACCCLI_0"),(String)row.get("YORAACCCLI_0")));
    	tmpBoolean = ((BigDecimal)row.get("YCODADR_0")).intValue() == 2 ? true : false;
    	item.setCodification(tmpBoolean);
    	item.setCodificationDate(x3UtrFaultLineDateConvert((Timestamp) row.get("YDTCODADR_0"),(String)row.get("YORACODADR_0")));
    	tmpBoolean = ((BigDecimal)row.get("YDISVER_0")).intValue() == 2 ? true : false;
    	item.setDrawingsVerified(tmpBoolean);
    	item.setDrawingsVerifiedCode((String)row.get("YCODICE_0"));
    	item.setDrawingsVerifiedDate(x3UtrFaultLineDateConvert((Timestamp) row.get("YDTUFFTEC_0"),(String)row.get("YORAUFFTEC_0")));
    	tmpBoolean = ((BigDecimal)row.get("YORDINE_0")).intValue() == 2 ? true : false;
    	item.setSalesOrder(tmpBoolean);
    	item.setSalesOrderQuantity(((BigDecimal)row.get("YPEZZIORD_0")).intValue());
    	item.setSalesOrderDate(x3UtrFaultLineDateConvert((Timestamp) row.get("YDTORDINE_0"),(String)row.get("YORAORDINE_0")));
    	tmpBoolean = ((BigDecimal)row.get("YPREVISTO_0")).intValue() == 2 ? true : false;
    	item.setPurchasePlan(tmpBoolean);
    	item.setPurchasePlanDate(x3UtrFaultLineDateConvert((Timestamp) row.get("YDTPREVISTO_0"),(String)row.get("YORAPREVISTO_0")));
    	tmpBoolean = ((BigDecimal)row.get("YACQNEW_0")).intValue() == 2 ? true : false;
    	item.setNewPurchase(tmpBoolean);
    	item.setNewPurchaseDate(x3UtrFaultLineDateConvert((Timestamp) row.get("YDTACQNEW_0"),(String)row.get("YORAACQNEW_0")));        	
		item.setToolDrawing(((BigDecimal)row.get("YATTREZ_0")).intValue());
		item.setToolDrawingDate(x3UtrFaultLineDateConvert((Timestamp) row.get("YDTATTREZ_0"),(String)row.get("YORAATTREZ_0")));
		tmpBoolean = ((BigDecimal)row.get("YATTROK_0")).intValue() == 2 ? true : false;
    	item.setToolCreation(tmpBoolean);
    	item.setToolCreationDate(x3UtrFaultLineDateConvert((Timestamp) row.get("YDTATTROK_0"),(String)row.get("YORAATTROK_0")));			
		tmpBoolean = ((BigDecimal)row.get("YTECNOL_0")).intValue() == 2 ? true : false;
    	item.setTechnology(tmpBoolean);
    	item.setTechnologyDate(x3UtrFaultLineDateConvert((Timestamp) row.get("YDTTECNOL_0"),(String)row.get("YORATECNOL_0")));
		tmpBoolean = ((BigDecimal)row.get("YQUALIT_0")).intValue() == 2 ? true : false;
    	item.setQuality(tmpBoolean);
    	item.setQualityDate(x3UtrFaultLineDateConvert((Timestamp) row.get("YDTQUALIT_0"),(String)row.get("YORAQUALIT_0")));
		item.setProjectState(((BigDecimal)row.get("YSTATOCA_0")).intValue());
    	item.setAddidionalDescription((String)row.get("YDESCRIZIONE_0"));

    	return item;
	}
	
	@Override
	public List<X3WarehouseWeightLine> findWeightSumLine(Date startDate, Date endDate, int weightQueryType) {
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				getWeightQuery(startDate, endDate, weightQueryType)
				,
                new Object[]{
                		"ATABDIV", 
                		"LNGDES", "POL", "6285",
                		dateHelper.getTime(startDate), 
                		dateHelper.getTime(endDate) 
                		}
				);
		List<X3WarehouseWeightLine> result = new ArrayList<>();
		X3WarehouseWeightLine item = null;
        for(Map<String,Object> row: resultSet ){
        	item = new X3WarehouseWeightLine();
        	item.setDate((Timestamp)row.get("CREDAT_0"));
        	item.setCardNumber(((String)row.get("CARDNR_0")));
        	item.setUserName(((String)row.get("TEXTE_0")));
        	item.setNumberOfLabels(((BigDecimal)row.get("QUANTITY")).intValue());
        	
        	result.add(item);
        }
		return result;
	}

	@Override
	public List<X3WarehouseWeightDetailLine> findWeightDetailLine(Date startDate, Date endDate, int weightQueryType) {
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				getWeightQuery(startDate, endDate, weightQueryType)
				,
				new Object[]{
						"ATABDIV", 
						"LNGDES", "POL", "6285",
						dateHelper.getTime(startDate), 
						dateHelper.getTime(endDate) 
				}
				);
		

		List<X3WarehouseWeightDetailLine> result = new ArrayList<>();
		X3WarehouseWeightDetailLine item = null;
		for(Map<String,Object> row: resultSet ){
			item = new X3WarehouseWeightDetailLine();
			item.setDateTime(x3WeightDateTimeConvert((Timestamp)row.get("CREDAT_0"), (String)row.get("CREHH_0")));
			item.setCardNumber(((String)row.get("CARDNR_0")));
			item.setUserName(((String)row.get("TEXTE_0")));
			item.setParcelNumber((String)row.get("YPRCNUM_0"));
			
			result.add(item);
		}
		return result;
	}

	private String getWeightQuery(Date startDate, Date endDate, int weightQueryType){
		String query = "";
		String company = "ATW";
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		if(weightQueryType == JdbcOracleX3Service.WEIGHT_QUERY_RECEPTION){
			// 1 = reception
			query =
					"SELECT "
					+ "PRC.CREDAT_0, "
					+ "TXT.TEXTE_0, "
					+ "Count(PRC.YPRCNUM_0) AS QUANTITY, "
					+ "PRC.CARDNR_0 "
					+ "FROM "
					+ company + ".YPARCELA PRC INNER JOIN " + company + ".ATEXTRA TXT "
					+ "ON "
					+ "PRC.CARDNR_0 = TXT.IDENT2_0 "
					+ "WHERE "
					+ "TXT.CODFIC_0 = ? "
					+ "AND "
					+ "TXT.ZONE_0 = ? "
					+ "AND "
					+ "TXT.LANGUE_0 = ? "
					+ "AND "
					+ "TXT.IDENT1_0 = ? "
					+ "GROUP BY "
					+ "PRC.CREDAT_0, "
					+ "TXT.TEXTE_0, "
					+ "PRC.CARDNR_0 "
					+ "HAVING "
					+ "PRC.CREDAT_0 >= ? AND PRC.CREDAT_0 <= ? "
					+ "ORDER BY "
					+ "PRC.CREDAT_0"
					;
		}
		else if (weightQueryType == JdbcOracleX3Service.WEIGHT_QUERY_SHIPMENT){
			// 2 = reception details	
			query =
					"SELECT "
					+ "PRC.CREDAT_0, "
					+ "TXT.TEXTE_0, "
					+ "Count(PRC.YPRCNUM_0) AS QUANTITY, "
					+ "PRC.CARDNR_0 "
					+ "FROM "
					+ company + ".YPARCEL PRC INNER JOIN " + company + ".ATEXTRA TXT "
					+ "ON "
					+ "PRC.CARDNR_0 = TXT.IDENT2_0 "
					+ "WHERE "
					+ "TXT.CODFIC_0 = ? "
					+ "AND "
					+ "TXT.ZONE_0 = ? "
					+ "AND "
					+ "TXT.LANGUE_0 = ? "
					+ "AND "
					+ "TXT.IDENT1_0 = ? "
					+ "GROUP BY "
					+ "PRC.CREDAT_0, "
					+ "TXT.TEXTE_0, "
					+ "PRC.CARDNR_0 "
					+ "HAVING "
					+ "PRC.CREDAT_0 >= ? AND PRC.CREDAT_0 <= ? "
					+ "ORDER BY "
					+ "PRC.CREDAT_0"
					;
		} else if (weightQueryType == JdbcOracleX3Service.WEIGHT_QUERY_RECEPTION_DETAIL){
			query = "SELECT "
					+ "PRC.CREDAT_0, "
					+ "PRC.CREHH_0, "
					+ "TXT.TEXTE_0, "
					+ "PRC.YPRCNUM_0, "
					+ "PRC.CARDNR_0 "
					+ "FROM "
					+ company + ".YPARCELA PRC INNER JOIN " + company + ".ATEXTRA TXT "
					+ "ON "
					+ "PRC.CARDNR_0 = TXT.IDENT2_0 "
					+ "WHERE "
					+ "TXT.CODFIC_0 = ? "
					+ "AND "
					+ "TXT.ZONE_0 = ? "
					+ "AND "
					+ "TXT.LANGUE_0 = ? "
					+ "AND "
					+ "TXT.IDENT1_0 = ? "
					+ "AND "
					+ "PRC.CREDAT_0 >= ? "
					+ "AND "
					+ "PRC.CREDAT_0 <= ? "
					+ "ORDER BY "
					+ "PRC.CREDAT_0"
					;
		} else if (weightQueryType == JdbcOracleX3Service.WEIGHT_QUERY_SHIPMENT_DETAIL){
			query = "SELECT "
					+ "PRC.CREDAT_0, "
					+ "PRC.CREHH_0, "
					+ "TXT.TEXTE_0, "
					+ "PRC.YPRCNUM_0, "
					+ "PRC.CARDNR_0 "
					+ "FROM "
					+ company + ".YPARCEL PRC INNER JOIN " + company + ".ATEXTRA TXT "
					+ "ON "
					+ "PRC.CARDNR_0 = TXT.IDENT2_0 "
					+ "WHERE "
					+ "TXT.CODFIC_0 = ? "
					+ "AND "
					+ "TXT.ZONE_0 = ? "
					+ "AND "
					+ "TXT.LANGUE_0 = ? "
					+ "AND "
					+ "TXT.IDENT1_0 = ? "
					+ "AND "
					+ "PRC.CREDAT_0 >= ? "
					+ "AND "
					+ "PRC.CREDAT_0 <= ? "
					+ "ORDER BY "
					+ "PRC.CREDAT_0"
					;
		}
		
		return query;
	}
	
	private Timestamp x3WeightDateTimeConvert(Timestamp date, String hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour.substring(0, 2)));
		cal.set(Calendar.MINUTE, Integer.parseInt(hour.substring(3, 5)));
		return new Timestamp(cal.getTimeInMillis());
	}

	@Override
	public Map<String, Integer> findAcvAverageUsageInPeriod(String startPeriod, String endPeriod, String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
					+ company + ".ITMMASTER.ITMREF_0, "
					//+ "Sum(" + company + ".XCSMED.XQTY_0) AS QTY, "
					+ "AVG(" + company + ".XCSMED.XQTY_0) AS AVERAGE "
					//+ "QTY/3 AS AVERAGE "
					+ "FROM "
					+ company + ".ITMMASTER INNER JOIN " + company + ".XCSMED "
					+ "ON "
					+ company + ".ITMMASTER.ITMREF_0 = " + company + ".XCSMED.XART_0 "
					+ "WHERE "
					+ company + ".ITMMASTER.TCLCOD_0 = ? "
					+ "AND "
					+ company + ".XCSMED.XPER_0 >= ? "
					+ "AND "
					+ company + ".XCSMED.XPER_0 <= ? "
					+ "GROUP BY "
					+ company + ".ITMMASTER.ITMREF_0"
				,
                new Object[]{"ACV", startPeriod, endPeriod}
				);
		
		Map <String, Integer> map = new HashMap<>();
		for(Map<String,Object> row: resultSet ){
			map.put(((String)row.get("ITMREF_0")), ((BigDecimal)row.get("AVERAGE")).intValue());
		}
			
		return map;
	}
	
	@Override
	public Map<String, Integer> findAcvMagStock(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ company + ".ITMMASTER.ITMREF_0, "
				+ "Sum("+ company + ".STOCK.QTYSTU_0) AS QUANTITY "
				+ "FROM "
				+ company + ".ITMMASTER INNER JOIN " + company + ".STOCK "
				+ "ON "
				+ company + ".ITMMASTER.ITMREF_0 = " + company + ".STOCK.ITMREF_0 "
				+ "WHERE "
				+ company + ".ITMMASTER.TCLCOD_0 = ? "
				+ "AND "
				+ company + ".STOCK.LOC_0 = ? "
				+ "GROUP BY "
				+ company + ".ITMMASTER.ITMREF_0"
				,
                new Object[]{"ACV", "MAG"}
				);
		
		Map <String, Integer> map = new HashMap<>();
		for(Map<String,Object> row: resultSet ){
			map.put(((String)row.get("ITMREF_0")), ((BigDecimal)row.get("QUANTITY")).intValue());
		}
			
		return map;
	}
	
	@Override
	public Map<String, Integer> findGeneralMagStock(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
						+ company + ".ITMMASTER.ITMREF_0, "
						+ "Sum("+ company + ".STOCK.QTYSTU_0) AS QUANTITY "
						+ "FROM "
						+ company + ".ITMMASTER INNER JOIN " + company + ".STOCK "
						+ "ON "
						+ company + ".ITMMASTER.ITMREF_0 = " + company + ".STOCK.ITMREF_0 "
						+ "WHERE "
						+ company + ".STOCK.LOC_0 = ? "
						+ "GROUP BY "
						+ company + ".ITMMASTER.ITMREF_0"
						,
						new Object[]{"MAG"}
				);
		
		Map <String, Integer> map = new HashMap<>();
		for(Map<String,Object> row: resultSet ){
			map.put(((String)row.get("ITMREF_0")), ((BigDecimal)row.get("QUANTITY")).intValue());
		}
		
		return map;
	}
	
	@Override
	public Map<String, Integer> findAcvShipStock(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
						+ company + ".ITMMASTER.ITMREF_0, "
						+ "Sum("+ company + ".STOCK.QTYSTU_0) AS QUANTITY "
						+ "FROM "
						+ company + ".ITMMASTER INNER JOIN " + company + ".STOCK "
						+ "ON "
						+ company + ".ITMMASTER.ITMREF_0 = " + company + ".STOCK.ITMREF_0 "
						+ "WHERE "
						+ company + ".ITMMASTER.TCLCOD_0 = ? "
						+ "AND ("
						+ company + ".STOCK.LOC_0 = ? "
						+ "OR "
						+ company + ".STOCK.LOC_0 = ? "
						+ "OR "						
						+ company + ".STOCK.LOC_0 = ? "
						+ ")"
						+ "GROUP BY "
						+ company + ".ITMMASTER.ITMREF_0"
						,
						new Object[]{"ACV", "QGX", "WGX01", "GEODE"}
				);
		
		Map <String, Integer> map = new HashMap<>();
		for(Map<String,Object> row: resultSet ){
			map.put(((String)row.get("ITMREF_0")), ((BigDecimal)row.get("QUANTITY")).intValue());
		}
		
		return map;
	}
	
	@Override
	public Map<String, Integer> findGeneralShipStock(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
						+ company + ".ITMMASTER.ITMREF_0, "
						+ "Sum("+ company + ".STOCK.QTYSTU_0) AS QUANTITY "
						+ "FROM "
						+ company + ".ITMMASTER INNER JOIN " + company + ".STOCK "
						+ "ON "
						+ company + ".ITMMASTER.ITMREF_0 = " + company + ".STOCK.ITMREF_0 "
						+ "WHERE "
						+ company + ".STOCK.LOC_0 = ? "
						+ "OR "
						+ company + ".STOCK.LOC_0 = ? "
						+ "OR "						
						+ company + ".STOCK.LOC_0 = ? "
						+ "GROUP BY "
						+ company + ".ITMMASTER.ITMREF_0"
						,
						new Object[]{"QGX", "WGX01", "GEODE"}
				);
		
		Map <String, Integer> map = new HashMap<>();
		for(Map<String,Object> row: resultSet ){
			map.put(((String)row.get("ITMREF_0")), ((BigDecimal)row.get("QUANTITY")).intValue());
		}
		
		return map;
	}
	
	@Override
	public List<X3ShipmentStockLineWithPrice> findAllShipStockWithAveragePrice(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "ITM.ITMREF_0, "
				+ "ITM.ITMDES1_0, "
				+ "ITM.TCLCOD_0, "
				+ "ITM.TSICOD_0, "
				+ "ITM.TSICOD_1, "
				+ "STK.QTYSTU_0, "
				+ "ITM.STU_0, "
				+ "ITV.AVC_0, "
				+ "STK.LOC_0 "
				+ "FROM ("
				+ company + ".STOCK STK INNER JOIN "
				+ company + ".ITMMASTER ITM "
				+ "ON STK.ITMREF_0 = ITM.ITMREF_0)"
				+ "INNER JOIN " + company + ".ITMMVT ITV "
				+ "ON ITM.ITMREF_0 = ITV.ITMREF_0 "
				+ "WHERE "
				+ "STK.LOC_0 = ? OR STK.LOC_0 = ? "
				+ "ORDER BY ITM.ITMREF_0, STK.LOC_0 "
				,
				new Object[]{"WGX01", "GEODE"}
		);
		
		List<X3ShipmentStockLineWithPrice> list = new ArrayList<>();
		X3ShipmentStockLineWithPrice line;
		for(Map<String,Object> row: resultSet ){
			line = new X3ShipmentStockLineWithPrice();
			
			line.setCode((String)row.get("ITMREF_0"));
			line.setDescription((String)row.get("ITMDES1_0"));
			line.setCategory((String)row.get("TCLCOD_0"));
			line.setGr1((String)row.get("TSICOD_0"));
			line.setGr2((String)row.get("TSICOD_1"));
			line.setUnit((String)row.get("STU_0"));
			line.setLocation((String)row.get("LOC_0"));
			line.setQuantity(((BigDecimal)row.get("QTYSTU_0")).doubleValue());
			line.setAveragePrice(((BigDecimal)row.get("AVC_0")).doubleValue());
		
			list.add(line);
		
		}
		
		return list;
	}
	
	@Override
	public List<String> findAcvNonProductionCodes(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ company + ".ITMMASTER.ITMREF_0 "
				+ "FROM "
				+ company + ".ITMMASTER LEFT JOIN " + company + ".BOMD "
				+ "ON "
				+ company + ".ITMMASTER.ITMREF_0 = " + company + ".BOMD.CPNITMREF_0 "
				+ "WHERE "
				+ company + ".ITMMASTER.TCLCOD_0 = ? "
				+ "AND "
				+ "NVL(" + company + ".BOMD.BOMSEQ_0, -1) = -1"
				+ "GROUP BY "
				+ company + ".ITMMASTER.ITMREF_0"
				,
                new Object[]{"ACV"}
				);
		
		List<String> list = new ArrayList<>();
		for(Map<String,Object> row: resultSet ){
			list.add((String)row.get("ITMREF_0"));
		}
			
		return list;
	}

	
	@Override
	public Map<String, X3ProductSellDemand> findAcvProductSellDemand(Date startDate, Date endDate, String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ company + ".ITMMASTER.ITMREF_0, "
				+ company + ".SORDERQ.QTY_0, "
				+ company + ".SORDERQ.SOHNUM_0, "
				+ company + ".SORDERQ.SOPLIN_0 "
				+ "FROM "
				+ company + ".SORDERQ INNER JOIN " + company + ".ITMMASTER "
				+ "ON "
				+ company + ".SORDERQ.ITMREF_0 = " + company + ".ITMMASTER.ITMREF_0 "
				+ "WHERE "
				+ company + ".SORDERQ.DEMDLVDAT_0 >= ? AND "
				+ company + ".SORDERQ.DEMDLVDAT_0 <= ? AND "
				+ company + ".ITMMASTER.TCLCOD_0 = ? AND "
				+ company + ".SORDERQ.SOQSTA_0 != ?"
				,
                new Object[]{startDate, endDate, "ACV", 3}
				);
		
		Map <String, X3ProductSellDemand> map = new HashMap<>();
		X3ProductSellDemand demand;
		String key;
		for(Map<String,Object> row: resultSet ){
			key = (String)row.get("ITMREF_0");
			if(map.containsKey(key)){
				map.get(key).addQuantity(((BigDecimal)row.get("QTY_0")).intValue());
				map.get(key).addOrder((String)row.get("SOHNUM_0"), ((BigDecimal)row.get("SOPLIN_0")).intValue());
			}
			else{
				demand = new X3ProductSellDemand();
				demand.setProductCode(key);
				demand.addQuantity(((BigDecimal)row.get("QTY_0")).intValue());
				demand.addOrder((String)row.get("SOHNUM_0"), ((BigDecimal)row.get("SOPLIN_0")).intValue());
				map.put(key, demand);
			}
			
		}
			
		return map;
	}

	@Override
	public List<DirectReceptionsShipmentLine> findDirectReceptionsShipmentLines(Date startDate, Date endDate, String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
				
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "SOQ.SOHNUM_0, "
				+ "SOQ.SOPLIN_0, "
				+ "(SOQ.QTYSTU_0 - SOQ.ODLQTYSTU_0 - SOQ.DLVQTY_0) AS LEFT_TO_SEND,  "
				+ "ITM.ITMREF_0, "
				+ "ITM.ITMDES1_0, "
				+ "SOQ.DEMDLVDAT_0, "
				+ "SOQ.QTY_0, "
				+ "BPR.BPRNAM_0, "
				+ "BPR.CRY_0 "
				+ "FROM ("
				+ "(" + company + ".SORDERQ SOQ INNER JOIN " + company + ".ITMMASTER ITM "
				+ "ON SOQ.ITMREF_0 = ITM.ITMREF_0) "
				+ "INNER JOIN " + company + ".SORDER SOR ON SOQ.SOHNUM_0 = SOR.SOHNUM_0) "
				+ "INNER JOIN " + company + ".BPARTNER BPR ON SOR.X_CODCLI_0 = BPR.BPRNUM_0 "
				+ "WHERE "
				+ "SOQ.DEMDLVDAT_0 >= ? AND "
				+ "SOQ.DEMDLVDAT_0 <= ? AND "
				+ "ITM.TCLCOD_0  = ? AND "
				+ "SOQ.SOQSTA_0 <> ? "
				+ "ORDER BY "
				+ "SOQ.SOHNUM_0, "
				+ "ITM.ITMREF_0, "
				+ "SOQ.DEMDLVDAT_0, "
				+ "BPR.BPRNAM_0"
				,
                new Object[]{startDate, endDate, "ACV", 3}
				);
		
		List<DirectReceptionsShipmentLine> list = new ArrayList<>();
		DirectReceptionsShipmentLine line;
		for(Map<String,Object> row: resultSet ){
			line = new DirectReceptionsShipmentLine();
			line.setOrderNumber(((String)row.get("SOHNUM_0")));
			line.setProductCode(((String)row.get("ITMREF_0")));
			line.setProductDescription(((String)row.get("ITMDES1_0")));
			line.setDemandedDate(((Timestamp)row.get("DEMDLVDAT_0")));
			line.setClientName(((String)row.get("BPRNAM_0")));
			line.setCountry(((String)row.get("CRY_0")));
			line.setLeftToSend(((BigDecimal)row.get("LEFT_TO_SEND")).intValue());
			line.setOrderedQuantity(((BigDecimal)row.get("QTY_0")).intValue());
			list.add(line);
		}
			
		return list;
	}

	@Override
	public Map<String, Map<Integer, Integer>> getAcvConsumptionListForYear(int year, String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "xcs.XART_0, "
				+ "xcs.XPER_0, "
				+ "xcs.XQTY_0 "
				+ "FROM " 
				+ company+ ".XCSMED xcs "
				+ "WHERE "
				+ "xcs.XPER_0 >= ? AND xcs.XPER_0 <= ? "
				,
                new Object[]{year+"01", year+"12"}
				);
		
		Map <String, Map<Integer, Integer>> result = new HashMap<>();
		String code;
		int period, qty;
		for(Map<String, Object> row: resultSet ){
			code = (String)row.get("XART_0");
			period = ((BigDecimal)row.get("XPER_0")).intValue();
			qty = ((BigDecimal)row.get("XQTY_0")).intValue();

			if(!result.containsKey(code)){
				result.put(code, new HashMap<Integer, Integer>());
			}
			result.get(code).put(period, qty);	
		}
		
		return result;
	}
	/*public Map<String, Integer> getAcvConsumptionListForYear(int year, String company) {
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "xcs.XART_0, "
				+ "Sum(xcs.XQTY_0) AS theSum "
				+ "FROM " 
				+ company+ ".XCSMED xcs "
				+ "WHERE "
				+ "xcs.XPER_0 >= ? AND xcs.XPER_0 <= ? "
				+ "GROUP BY xcs.XART_0"
				,
                new Object[]{year+"01", year+"12"}
				);
		
		Map <String, Integer> result = new HashMap<>();
		for(Map<String,Object> row: resultSet ){
				result.put((String)row.get("XART_0"), ((BigDecimal)row.get("theSum")).intValue());
		}
	
		return result;
	}*/

	@Override
	/**
	 * getsALLdemand, not only ACV
	 */
	public Map<String, Integer> getAcvDemandList(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "Sum(ord.RMNEXTQTY_0) AS theSum, "
				+ "ord.ITMREF_0 "
				+ "FROM "
				+ company + ".ORDERS ord "
				+ "WHERE "
				+ "(ord.WIPTYP_0 = 1 OR ord.WIPTYP_0 = 6) "
				+ "AND "
				+ "ord.WIPSTA_0 <= 3 "
				+ "GROUP BY "
				+ "ord.ITMREF_0"	
				,
                new Object[]{}
				);
		
		Map <String, Integer> result = new HashMap<>();
		for(Map<String,Object> row: resultSet ){
				result.put((String)row.get("ITMREF_0"), ((BigDecimal)row.get("theSum")).intValue());
		}
	
		return result;
	}
	
	@Override
	public Map<String, String> getAcvProductsEnglishDescriptions(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "txt.IDENT1_0, "
				+ "txt.TEXTE_0 "
				+ "FROM "
				+ company + ".ATEXTRA txt INNER JOIN "+ company + ".ITMMASTER itm "
				+ "ON itm.ITMREF_0 = txt.IDENT1_0 "
				+ "WHERE "
				+ "txt.CODFIC_0 = ? "
				+ "AND "
				+ "txt.LANGUE_0 = ? "
				+ "AND "
				+ "txt.ZONE_0 = ? "
				+ "AND "
				+ "itm.TCLCOD_0 = ? "
				,
                new Object[]{"ITMMASTER", "ENG", "DES1AXX", "ACV"}
				);
		
		Map <String, String> result = new HashMap<>();
		for(Map<String,Object> row: resultSet ){
				result.put((String)row.get("IDENT1_0"), ((String)row.get("TEXTE_0")));
		}
		
		return result;
	}
	
	@Override
	public Map<String, X3ConsumptionSupplyInfo> getAcvListOfLastSupplyInfo(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "prcd.ITMREF_0, "
				+ "prct.RCPDAT_0, "
				+ "prct.BPSNUM_0, "
				+ "prct.BPONAM_0 "
				+ "FROM ("
				+ company + ".PRECEIPTD prcd INNER JOIN " + company + ".PRECEIPT prct "
				+ "ON prcd.PTHNUM_0 = prct.PTHNUM_0"
				+ ") "
				+ "INNER JOIN " + company + ".ITMMASTER itm "
				+ "ON prcd.ITMREF_0 = itm.ITMREF_0 "
				+ "WHERE "
				+ "itm.TCLCOD_0 = ? "
				,
                new Object[]{"ACV"}
				);
		
		Map <String, X3ConsumptionSupplyInfo> map = new HashMap<>();
		X3ConsumptionSupplyInfo info;
		String key;
		for(Map<String,Object> row: resultSet ){
			key = (String)row.get("ITMREF_0");
			info = new X3ConsumptionSupplyInfo();
			info.setProductCode(key);
			info.setDate((Timestamp)row.get("RCPDAT_0"));
			info.setSupplierCode((String)row.get("BPSNUM_0"));
			info.setName((String)row.get("BPONAM_0"));
			map.put(key, info);
		}
			
		return map;
	}

	@Override
	public List<X3ConsumptionProductInfo> getAcvListForConsumptionReport(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "itm.ITMREF_0, "
				+ "itm.YFAMGROUP_0, "
				+ "itm.ITMDES1_0, "
				+ "itv.PHYSTO_0, "
				+ "itv.ORDSTO_0, "
				+ "itv.AVC_0,"
				+ "itf.BUY_0, "
				+ "itf.OFS_0, "
				+ "itf.SAFSTO_0, "
				+ "itf.REOTSD_0, "
				+ "itf.MAXSTO_0, "
				+ "itf.REOMINQTY_0, "
				+ "itf.MFGLOTQTY_0, "
				+ "itf.YLTACQ_0, "
				+ "itv.LASRCPDAT_0, "
				+ "itv.LASISSDAT_0 "
				+ "FROM ("
				+ company + ".ITMMASTER itm INNER JOIN " + company + ".ITMMVT itv "
				+ "ON itm.ITMREF_0 = itv.ITMREF_0"
				+ ") "
				+ "INNER JOIN " + company + ".ITMFACILIT itf "
				+ "ON itm.ITMREF_0 = itf.ITMREF_0 "
				+ "WHERE itm.TCLCOD_0 = ? "

				,
                new Object[]{"ACV"}
				);

		List<X3ConsumptionProductInfo> list = new ArrayList<>();
		X3ConsumptionProductInfo info;
		for(Map<String,Object> row: resultSet ){
			info = new X3ConsumptionProductInfo();
			info.setProductCode((String)row.get("ITMREF_0"));
			info.setProductDescriptionPl((String)row.get("ITMDES1_0"));
			info.setStock(((BigDecimal)row.get("PHYSTO_0")).intValue());
			info.setInOrder(((BigDecimal)row.get("ORDSTO_0")).intValue());
			info.setAverageCost(((BigDecimal)row.get("AVC_0")).doubleValue());
			info.setBuyerCode((String)row.get("BUY_0"));
			info.setBuyGroupCode((String)row.get("YFAMGROUP_0"));
			info.setLeadTime(((BigDecimal)row.get("YLTACQ_0")).intValue());
			info.setSafetyStock(((BigDecimal)row.get("SAFSTO_0")).intValue());
			info.setReorderPoint(((BigDecimal)row.get("REOTSD_0")).intValue());
			info.setMaxStsock(((BigDecimal)row.get("MAXSTO_0")).intValue());
			info.setEwz(((BigDecimal)row.get("REOMINQTY_0")).intValue());
			info.setTechnicalLot(((BigDecimal)row.get("MFGLOTQTY_0")).intValue());
			info.setLastReceptionDate(((Timestamp)row.get("LASRCPDAT_0")));
			info.setLastIssueDate(((Timestamp)row.get("LASISSDAT_0")));
			
			
			list.add(info);
		}
			
		return list;
	}

	@Override
	public List<X3SalesOrderLine> findOpenedSalesOrderLinesInPeriod(Date startDate, Date endDate, String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "SOQ.SOHNUM_0, "
				+ "SOQ.SOPLIN_0, "
				+ "SOQ.QTYSTU_0, "
				+ "(SOQ.QTYSTU_0 - SOQ.ODLQTYSTU_0 - SOQ.DLVQTY_0) AS LEFT_TO_SEND,  "
				+ "SOQ.DEMDLVDAT_0, "
				+ "SOP.X_DATAORI_0, "
				+ "SOQ.CREDAT_0, "
				+ "SOQ.UPDDAT_0, "
				+ "SOQ.DEMSTA_0, "
				+ "SOP.NETPRI_0, "
				+ "SOR.CUR_0, "
				+ "SOR.CHGRAT_0, "
				+ "ITM.ITMREF_0, "
				+ "ITM.ITMDES1_0, "
				+ "ITM.TCLCOD_0, "
				+ "ITM.TSICOD_0, "
				+ "ITM.TSICOD_1, "
				+ "ITM.TSICOD_2, "
				+ "BPR.BPRNUM_0, "
				+ "BPR.BPRNAM_0, "
				+ "BPR.CRY_0 "
				
				+ "FROM ("
				+ "("
				+ "(" + company + ".SORDERQ SOQ INNER JOIN " + company + ".SORDERP SOP "
				+ "ON SOQ.SOHNUM_0 = SOP.SOHNUM_0 AND "
				+ "SOQ.SOPLIN_0 = SOP.SOPLIN_0 AND "
				+ "SOQ.SOQSEQ_0 = SOP.SOPSEQ_0 "
				+ ") "
				+ "INNER JOIN " + company + ".ITMMASTER ITM "
				+ "ON SOQ.ITMREF_0 = ITM.ITMREF_0) "
				+ "INNER JOIN " + company + ".SORDER SOR ON SOQ.SOHNUM_0 = SOR.SOHNUM_0) "
				+ "INNER JOIN " + company + ".BPARTNER BPR ON SOR.X_CODCLI_0 = BPR.BPRNUM_0 "
				
				
				+ "WHERE "
				+ "SOQ.DEMDLVDAT_0 >= ? AND "
				+ "SOQ.DEMDLVDAT_0 <= ? AND "
				+ "SOQ.SOQSTA_0 <> ? "
				+ "ORDER BY "
				+ "SOQ.DEMDLVDAT_0, "
				+ "SOQ.SOHNUM_0, "
				+ "ITM.ITMREF_0, "
				+ "BPR.BPRNAM_0 "
				,
                new Object[]{startDate, endDate, 3}
				);
		
		List<X3SalesOrderLine> list = new ArrayList<>();
		X3SalesOrderLine line;
		for(Map<String,Object> row: resultSet ){
			line = new X3SalesOrderLine();
			line.setOrderNumber((String)row.get("SOHNUM_0"));
			line.setOrderLineNumber(((BigDecimal)row.get("SOPLIN_0")).intValue());
			line.setProductCode((String)row.get("ITMREF_0"));
			line.setProductDescription((String)row.get("ITMDES1_0"));
			line.setProductCategory((String)row.get("TCLCOD_0"));
			line.setProductGr1((String)row.get("TSICOD_0"));
			line.setProductGr2((String)row.get("TSICOD_1"));
			line.setProductGr3((String)row.get("TSICOD_2"));
			line.setClientCode((String)row.get("BPRNUM_0"));
			line.setClientName(((String)row.get("BPRNAM_0")));
			line.setCountry(((String)row.get("CRY_0")));
			line.setDemandedDate(((Timestamp)row.get("DEMDLVDAT_0")));
			line.setOriginalDate(((Timestamp)row.get("X_DATAORI_0")));
			line.setCreationDate(((Timestamp)row.get("CREDAT_0")));
			line.setUpdateDate(((Timestamp)row.get("UPDDAT_0")));
			line.setQuantityLeftToSend(((BigDecimal)row.get("LEFT_TO_SEND")).intValue());
			line.setQuantityOrdered(((BigDecimal)row.get("QTYSTU_0")).intValue());
			line.setUnitPrice(((BigDecimal)row.get("NETPRI_0")).doubleValue());
			line.setExchangeRate(((BigDecimal)row.get("CHGRAT_0")).doubleValue());
			line.setCurrency((String)row.get("CUR_0"));
			line.setDemandState(((BigDecimal)row.get("DEMSTA_0")).intValue());
			list.add(line);
		}
		return list;
	}

	@Override
	public Map<String, Integer> findGeneralStockForAllProducts(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "itv.ITMREF_0, "
				+ "itv.PHYSTO_0 "
				+ "FROM " 
				+ company + ".ITMMVT itv "
				,
                new Object[]{}
				);
		
		Map<String, Integer> map = new HashMap<>();
		for(Map<String,Object> row: resultSet ){
			map.put((String)row.get("ITMREF_0"), ((BigDecimal)row.get("PHYSTO_0")).intValue());
		}
		return map;
	}

	@Override
	public X3Workstation findWorkstationByCode(String company, String code) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "WST.WST_0, "
				+ "TXT.TEXTE_0 "
				+ "FROM "
				+ company + ".WORKSTATIO WST "
				+ "INNER JOIN "
				+ company + ".ATEXTRA TXT "
				+ "ON "
				+ "WST.WST_0 = TXT.IDENT1_0 "
				+ "WHERE TXT.CODFIC_0 = ? "
				+ "AND TXT.ZONE_0 = ? "
				+ "AND TXT.LANGUE_0 = ? "
				+ "AND UPPER(WST.WST_0) = ? "
				+ "ORDER BY WST.WST_0 ASC"
				+ "",
                new Object[]{"WORKSTATIO", "WSTDESAXX", "POL", code.toUpperCase()});
        		
		X3Workstation workstation = null;
		
        for(Map<String,Object> row: resultSet ){
        	workstation = new X3Workstation();
        	workstation.setCode((String)row.get("WST_0"));
        	workstation.setName(((String)row.get("TEXTE_0")));
        }
		
		return workstation;
	}
	
	@Override
	public List<X3Workstation> getWorkstations(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "WST.WST_0, "
				+ "TXT.TEXTE_0 "
				+ "FROM "
				+ company + ".WORKSTATIO WST "
				+ "INNER JOIN "
				+ company + ".ATEXTRA TXT "
				+ "ON "
				+ "WST.WST_0 = TXT.IDENT1_0 "
				+ "WHERE TXT.CODFIC_0 = ? "
				+ "AND TXT.ZONE_0 = ? "
				+ "AND TXT.LANGUE_0 = ? "
				+ "ORDER BY WST.WST_0 ASC"
				,
				new Object[]{"WORKSTATIO", "WSTDESAXX", "POL"});
		
		X3Workstation workstation = null;
		List<X3Workstation> result = new ArrayList<>();
		for(Map<String,Object> row: resultSet ){
			workstation = new X3Workstation();
			workstation.setCode((String)row.get("WST_0"));
			workstation.setName(((String)row.get("TEXTE_0")));
			result.add(workstation);
		}
		
		return result;
	}

	@Override
	public boolean checkIfLocationExist(String company, String location) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
						"SELECT " 
						+ company + ".STOLOC.LOC_0 "
						+ "FROM " 
						+ company + ".STOLOC " 
						+ "WHERE " 
						+ company +".STOLOC.LOC_0 = ?",
                new Object[]{location});

		return !resultSet.isEmpty();
	}

	@Override
	public String findPackageDescription(String company, String packageCode) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ company + ".ATEXTRA.TEXTE_0 "
				+ "FROM " 
				+ company + ".ATEXTRA "
				+ "WHERE "
				+ "UPPER(" + company + ".ATEXTRA.IDENT2_0) = ? AND "
				+ company + ".ATEXTRA.CODFIC_0 = ? AND "
				+ company + ".ATEXTRA.ZONE_0 = ? AND "
				+ company + ".ATEXTRA.LANGUE_0 = ? AND "
				+ company + ".ATEXTRA.IDENT1_0 = ? "
				, new Object[] {
						packageCode.toUpperCase(),
						"ATABDIV",
						"LNGDES",
						"POL",
						"6283"
						});
        String result = null;
        for(Map<String,Object> row: resultSet ){
        	result = (String)row.get("TEXTE_0");
        }
		return result;
	}

	@Override
	public Map<String, String> getDescriptionsByLanguage(String x3lang, String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(""
				+ "SELECT "
				+ "TXT.IDENT1_0, "
				+ "TXT.TEXTE_0 "
				+ "FROM "
				+ company + ".ATEXTRA TXT "
				+ "WHERE "
				+ "TXT.CODFIC_0 = ? "
				+ "AND TXT.LANGUE_0 = ? "
				+ "AND TXT.ZONE_0 = ? "
				,
                new Object[]{"ITMMASTER", x3lang, "DES1AXX"}
				);
		
		Map <String, String> result = new HashMap<>();
		for(Map<String,Object> row: resultSet ){
				result.put((String)row.get("IDENT1_0"), ((String)row.get("TEXTE_0")));
		}
		
		return result;
	}

	@Override
	public List<X3UsageDetail> getAcvUsageDetailsListByYear(int year, String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(""
				+ "SELECT "
				+ "STJ.ITMREF_0, "
				+ "STJ.CREDAT_0, "
				+ "(-1*STJ.QTYSTU_0) AS QTY "
				+ "FROM "
				+ company + ".STOJOU STJ INNER JOIN " + company + ".ITMMASTER ITM "
				+ "ON "
				+ "STJ.ITMREF_0 = ITM.ITMREF_0 "
				+ "WHERE ("
				+ "STJ.TRSTYP_0 = 4 "
				+ "OR "
				+ "STJ.TRSTYP_0 = 6 "
				+ ") "
				+ "AND "
				+ "EXTRACT(YEAR FROM STJ.CREDAT_0 ) = ? "
				+ "AND "
				+ "ITM.TCLCOD_0 = ? "
				+ ""
				,
                new Object[]{year, "ACV"}
				);
		
		List <X3UsageDetail> result = new ArrayList<>();
		X3UsageDetail usage;
		for(Map<String,Object> row: resultSet ){
			usage = new X3UsageDetail();
			usage.setProductCode((String)row.get("ITMREF_0"));			
			usage.setUsageDate((Timestamp)row.get("CREDAT_0"));
			usage.setUsage(((BigDecimal)row.get("QTY")).intValue());
			result.add(usage);
		}
		
		return result;
	}

	@Override
	public List<X3CoverageData> getCoverageInitialData(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
				
		List<Map<String,Object>> resultSet = jdbc.queryForList(""
				+ "SELECT "
				+ "ITM.ITMREF_0, "
				+ "ITM.TSICOD_1, "
				+ "ITF.OFS_0, "
				+ "ITF.REOPOL_0, "
				+ "ITV.YQTAPRERIC_0, "
				+ "ITV.PHYSTO_0, "
				+ "ITV.ORDSTO_0, "
				+ "ITF.BUY_0, "
				+ "USR.NOMUSR_0 "
				+ "FROM (("
				+ company + ".ITMMASTER ITM INNER JOIN "
				+ company + ".ITMMVT ITV ON ITM.ITMREF_0 = ITV.ITMREF_0) "
				+ "INNER JOIN "
				+ company + ".ITMFACILIT ITF ON "
				+ "ITV.ITMREF_0 = ITF.ITMREF_0) "
				+ "INNER JOIN "
				+ company + ".AUTILIS USR ON "
				+ "ITF.BUY_0 = USR.USR_0 "
				+ "WHERE ITM.TCLCOD_0 = ? "
				+ "ORDER BY ITM.ITMREF_0 ASC"
				,
                new Object[]{"ACV"}
				);
        
        List<X3CoverageData> result = new ArrayList<>();
        X3CoverageData data;
        for(Map<String,Object> row: resultSet ){
        	data = new X3CoverageData();
        	data.setCode((String)row.get("ITMREF_0"));
        	data.setBuyerCode((String)row.get("BUY_0"));
        	data.setBuyerName((String)row.get("NOMUSR_0"));
        	data.setGr2((String)row.get("TSICOD_1"));
        	data.setReorderPolicy((String)row.get("REOPOL_0"));        	
        	data.setInOrder(((BigDecimal)row.get("ORDSTO_0")).intValue());
        	data.setStock(((BigDecimal)row.get("PHYSTO_0")).intValue());
        	data.setTime(((BigDecimal)row.get("OFS_0")).intValue());
        	data.setInRoute(((BigDecimal)row.get("YQTAPRERIC_0")).intValue());
        	

        	result.add(data);
        }
        
		return result;
	}

	@Override
	/*
	 * Map <product code, first supplier>
	 */
	public Map<String, X3Supplier> getFirstAcvSuppliers(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
				
		List<Map<String,Object>> resultSet = jdbc.queryForList( ""
				+ "SELECT "
				+ "POQ.ITMREF_0, "
				+ "POQ.ORDDAT_0, "
				+ "POQ.BPSNUM_0, "
				+ "BPS.BPSNAM_0 "
				+ "FROM "
				+ company + ".PORDERQ POQ INNER JOIN " + company + ".BPSUPPLIER BPS "
				+ "ON "
				+ "POQ.BPSNUM_0 = BPS.BPSNUM_0 "
				+ "ORDER BY POQ.ORDDAT_0 DESC"
				,
                new Object[]{});
		
        Map <String, X3Supplier> map = new HashMap<>();
        X3Supplier supplier;
        for(Map<String,Object> row: resultSet ){
        	supplier = new X3Supplier();
        	supplier.setCode((String)row.get("BPSNUM_0"));
        	supplier.setName((String)row.get("BPSNAM_0"));
        	supplier.setFirstOrderDate((Timestamp)row.get("ORDDAT_0"));
        	map.put((String)row.get("ITMREF_0"), supplier);
        }
		return map;
	}

	@Override
	public List<X3SalesOrderItem> findAllSalesOrdersAfvItemsInPeriod(Date startDate, Date endDate, String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "SOQ.SOHNUM_0, "
				+ "SOQ.ITMREF_0, "
				+ "SOQ.DEMDLVDAT_0, "
				+ "ITM.ITMDES1_0, "
				+ "Sum(SOQ.QTY_0) AS qtySum "
				+ "FROM "
				+ company + ".SORDERQ SOQ INNER JOIN " + company + ".ITMMASTER ITM "
				+ "ON "
				+ "SOQ.ITMREF_0 = ITM.ITMREF_0 "
				+ "WHERE "
				+ "SOQ.DEMDLVDAT_0 > = ? AND SOQ.DEMDLVDAT_0 <= ? AND ITM.TCLCOD_0 = ?"
				+ "GROUP BY "
				+ "SOQ.SOHNUM_0, "
				+ "SOQ.DEMDLVDAT_0, "
				+ "SOQ.ITMREF_0, "
				+ "ITM.ITMDES1_0 "
				+ "ORDER BY "
				+ "SOQ.SOHNUM_0, "
				+ "SOQ.ITMREF_0 ",
                new Object[]{startDate, endDate, "AFV"});
        
		List<X3SalesOrderItem> result = new ArrayList<>();
		X3SalesOrderItem order = null;
		
        for(Map<String,Object> row: resultSet ){
        	order = new X3SalesOrderItem();
        	order.setOrderNumber((String)row.get("SOHNUM_0"));
        	order.setDemandedDate((Timestamp)row.get("DEMDLVDAT_0"));
        	order.setProductCode((String)row.get("ITMREF_0"));
        	order.setProductDescription((String)row.get("ITMDES1_0"));
        	order.setQuantityOrdered(((BigDecimal)row.get("qtySum")).intValue());
        	result.add(order);
        }
        
		return result;
	}

	@Override
	public List<X3ToolEntry> getAllToolsInRouting(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT " 
				+ company + ".ROUOPE.ITMREF_0, "
				+ company + ".ROUOPE.XATTR_0, "
				+ company + ".ROUOPE.OPENUM_0, "
				+ company + ".ROUOPE.WST_0 "
                + "FROM " + company + ".ROUOPE "
            	+ "WHERE " 
            	+ "LENGTH(TRIM(" + company + ".ROUOPE.XATTR_0)) > 0 ",
                new Object[]{});

        List<X3ToolEntry> result = new ArrayList<>();
        X3ToolEntry entry;
        for(Map<String,Object> row: resultSet ){
        	entry = new X3ToolEntry();
        	entry.setCode((String)row.get("ITMREF_0"));
        	entry.setTool((String)row.get("XATTR_0"));
        	entry.setOperation(((BigDecimal)row.get("OPENUM_0")).intValue());
        	entry.setMachine((String)row.get("WST_0"));
        	result.add(entry);
        }
        
		return result;
	}

	@Override
	public List<X3KeyValString> getAllBomPartsInBoms(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ company + ".BOMD.CPNITMREF_0, "
				+ company + ".BOMD.ITMREF_0 "
				+ "FROM "
				+ company + ".BOMD ",
                new Object[]{});
        
        List<X3KeyValString> result = new ArrayList<>();
        for(Map<String,Object> row: resultSet ){
        	result.add(new X3KeyValString((String)row.get("ITMREF_0"), (String)row.get("CPNITMREF_0")));
        }
        
		return result;
	}

	@Override
	public Map<String, X3StoreInfo> getX3StoreInfoByCode(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "STK.ITMREF_0, "
				+ "STK.QTYSTU_0, "
				+ "STK.LOC_0 "
				+ "FROM " + company + ".STOCK STK",
                new Object[]{});
        
		Map<String, X3StoreInfo> result = new HashMap<>();
		String code;
		int quantity;
        for(Map<String,Object> row: resultSet ){
        	code = (String)row.get("ITMREF_0");
        	quantity = ((BigDecimal)row.get("QTYSTU_0")).intValue();
        	
        	// create entry if not exist
        	if(!result.containsKey(code)){
        		result.put(code, new X3StoreInfo(code));
        	}
        	
        	// set quantity        	
        	switch ((String)row.get("LOC_0")){
        		case "MAG":
        			result.get(code).addMag(quantity);
        			break;
        		case "QGX":
        			result.get(code).addQgx(quantity);
        			break;
        		case "WGX01":
        			result.get(code).addWgx(quantity);
        			break;
        		case "GEODE":
        			result.get(code).addGeode(quantity);
        			break;
        		default:
        			result.get(code).addGeneralStore(quantity);
        			break;
        	}
        }
        
		return result;
	}

	@Override
	public Map<String, String> getVariousTableData(String company, String table, String x3language) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList( ""
				+ "SELECT "
				+ "TXT.IDENT2_0, "
				+ "TXT.TEXTE_0 "
				+ "FROM "
				+ company + ".ATEXTRA TXT "
				+ "WHERE "
				+ "TXT.CODFIC_0 = ? "
				+ "AND TXT.ZONE_0 = ? "
				+ "AND TXT.LANGUE_0 = ? "
				+ "AND TXT.IDENT1_0 = ?"
				,
                new Object[]{"ATABDIV", "LNGDES", x3language, table });
		
        Map <String, String> map = new HashMap<>();
        for(Map<String,Object> row: resultSet ){
        	map.put((String)row.get("IDENT2_0"), (String)row.get("TEXTE_0"));
        }
		return map;
	}

	@Override
	public Map<String, Integer> findStockByLocation(String company, String location) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList( ""
				+ "SELECT "
				+ "STK.ITMREF_0, "
				+ "Sum(STK.QTYSTU_0) AS QTY "
				+ "FROM "
				+ company + ".STOCK STK "
				+ "WHERE"
				+ " STK.LOC_0 = ? "
				+ "GROUP BY "
				+ "STK.ITMREF_0"
				,
                new Object[]{location.toUpperCase()});
		
        Map <String, Integer> map = new HashMap<>();
        for(Map<String,Object> row: resultSet ){
        	map.put((String)row.get("ITMREF_0"), ((BigDecimal)row.get("QTY")).intValue());
        }
        
        return map;
	}
	
	@Override
	public Map<String, Integer> findStockByState(String state, String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList( ""
				+ "SELECT "
				+ "STK.ITMREF_0, "
				+ "Sum(STK.QTYSTU_0) AS QTY "
				+ "FROM "
				+ company + ".STOCK STK "
				+ "WHERE"
				+ " STK.STA_0 = ? "
				+ "GROUP BY "
				+ "STK.ITMREF_0"
				,
				new Object[]{state.toUpperCase()});
		
		Map <String, Integer> map = new HashMap<>();
		for(Map<String,Object> row: resultSet ){
			map.put((String)row.get("ITMREF_0"), ((BigDecimal)row.get("QTY")).intValue());
		}
		
		return map;
	}

	@Override
	public Map<String, List<X3BomItem>> getAllBomPartsTopLevel(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		Timestamp now = dateHelper.getCurrentTime();
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ company + ".BOMD.ITMREF_0, "
			    + company + ".BOMD.BOMSEQ_0, "
				+ company + ".BOMD.CPNITMREF_0, "
				+ company + ".BOMD.LIKQTY_0, "
				+ company + ".ITMMASTER.STU_0, "
				+ company + ".BOMD.BOMSTRDAT_0, "
				+ company + ".BOMD.BOMENDDAT_0, "
				+ company + ".ITMMASTER.ITMDES1_0, "
				+ company + ".ITMMASTER.ITMDES2_0 "
				+ "FROM "
				+ company + ".ITMMASTER INNER JOIN " + company + ".BOMD "
				+ "ON "
				+ company + ".ITMMASTER.ITMREF_0 = " + company + ".BOMD.CPNITMREF_0 "
				+ "WHERE "
				+ company + ".BOMD.BOMALT_0 = 1 "
				+ "AND ("
					+ company + ".BOMD.BOMSTRDAT_0 < ? "
					+ "OR "
					+ company + ".BOMD.BOMSTRDAT_0 = TO_TIMESTAMP('1599/12/31 00:00:00')"
				+ ")"
				+ "AND ("
					+ company + ".BOMD.BOMENDDAT_0 > ? "
					+ "OR "
					+ company + ".BOMD.BOMENDDAT_0 = TO_TIMESTAMP('1599/12/31 00:00:00')"
				+ ") "
				+ "ORDER BY "
				+ company + ".BOMD.BOMSEQ_0",
                new Object[]{now, now});
        

		Map<String, List<X3BomItem>> map = new HashMap<>();
		X3BomItem item;
		String parent;
		
        for(Map<String,Object> row: resultSet ){
        	parent = (String)row.get("ITMREF_0");
        	item = new X3BomItem();
        	item.setSequence(((BigDecimal)row.get("BOMSEQ_0")).intValue());
        	item.setPartCode((String)row.get("CPNITMREF_0"));
        	item.setPartDescription(((String)row.get("ITMDES1_0")) + " " +  ((String)row.get("ITMDES2_0")));
        	item.setModelUnit((String)row.get("STU_0"));
        	item.setModelQuantity(((BigDecimal)row.get("LIKQTY_0")).doubleValue());
        	
        	if(!map.containsKey(parent)){
        		map.put(parent, new ArrayList<X3BomItem>());
        	}
        	
        	map.get(parent).add(item);
        }
        
        return map;
	}
	
	@Override
	public List<X3BomPart> getAllBomEntries(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		Timestamp now = dateHelper.getCurrentTime();
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ company + ".BOMD.ITMREF_0, "
				+ company + ".BOMD.CPNITMREF_0, "
				+ company + ".BOMD.LIKQTY_0, "
				+ company + ".BOMD.BOMSTRDAT_0, "
				+ company + ".BOMD.BOMENDDAT_0 "
				+ "FROM " + company + ".BOMD "
				+ "WHERE "
				+ company + ".BOMD.BOMALT_0 = 1 "
				+ "AND ("
					+ company + ".BOMD.BOMSTRDAT_0 < ? "
					+ "OR "
					+ company + ".BOMD.BOMSTRDAT_0 = TO_TIMESTAMP('1599/12/31 00:00:00')"
				+ ")"
				+ "AND ("
					+ company + ".BOMD.BOMENDDAT_0 > ? "
					+ "OR "
					+ company + ".BOMD.BOMENDDAT_0 = TO_TIMESTAMP('1599/12/31 00:00:00')"
				+ ") "
				+ "ORDER BY "
				+ company + ".BOMD.BOMSEQ_0",
                new Object[]{now, now});
        

		List<X3BomPart> result = new ArrayList<>();
		X3BomPart item;
		
        for(Map<String,Object> row: resultSet ){
        	item = new X3BomPart();
        	item.setParentCode((String)row.get("ITMREF_0"));
        	item.setPartCode((String)row.get("CPNITMREF_0"));
        	item.setQuantityOfSubcode(((BigDecimal)row.get("LIKQTY_0")).doubleValue());
        	item.setQuantityOfSelf(1.0);
        	result.add(item);
        }
        
        return result;
	}
	

	@Override
	public Map<String, X3StandardCostEntry> getLastStandardCostsListFromCalculationTable(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT  "
				+ "x.code, "
				+ "x.calcdate, "
				+ "x.totalcost, "
				+ "x.materialcost, "
				+ "x.machinecost, "
				+ "x.labourcost "
				+ "FROM "
				+ "	(SELECT "
				+ "		ITMREF_0 as code, "
				+ "		ITCDAT_0 as calcdate,"
				+ "		CSTTOT_0 as totalcost, "
				+ "		MATTOT_0 as materialcost, "
				+ "		MACTOT_0 as machinecost, "
				+ "		LABTOT_0 as labourcost, "
				+ "		rank() over (partition by ITMREF_0 order by ITCDAT_0 desc) as rank "
				+ "	FROM "
				+ "		" + company + ".ITMCOST "
				+ "	WHERE "
				+ "		CSTTYP_0 = 1"
				+ "	) x "
				+ "WHERE x.rank = 1"
				,
                new Object[]{});
        
		Map<String, X3StandardCostEntry> costs = new HashMap<>();
		X3StandardCostEntry entry;
        String code;
		for(Map<String,Object> row: resultSet ){
			code = (String)row.get("code");
			entry = new X3StandardCostEntry();
			entry.setCode(code);
			entry.setDate((Timestamp)row.get("calcdate"));
			entry.setLabourCost(((BigDecimal)row.get("labourcost")).doubleValue());
			entry.setMachineCost(((BigDecimal)row.get("machinecost")).doubleValue());
			entry.setMaterialCost(((BigDecimal)row.get("materialcost")).doubleValue());
			entry.setTotalCost(((BigDecimal)row.get("totalcost")).doubleValue());
        	costs.put(code, entry);
        }
		return costs;
	}
	
	@Override
	public Map<String, X3StandardCostEntry> getStandardCostsMap(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "VCSAT.ITMREF_0, "
				+ "VCSAT.TOTAL_0, "
				+ "VCSAT.MATERIAL_0, "
				+ "VCSAT.MACHINE_0, "
				+ "VCSAT.LABOUR_0, "
				+ "VCSAT.CSTDATE_0 "
				+ "FROM "
				+ company + ".VSTDCST VCSAT",
                new Object[]{});
        
		Map<String, X3StandardCostEntry> costs = new HashMap<>();
		X3StandardCostEntry entry;
        String code;
		for(Map<String,Object> row: resultSet ){
			code = (String)row.get("ITMREF_0");
			entry = new X3StandardCostEntry();
			entry.setCode(code);
			entry.setDate((Timestamp)row.get("CSTDATE_0"));
			entry.setLabourCost(((BigDecimal)row.get("LABOUR_0")).doubleValue());
			entry.setMachineCost(((BigDecimal)row.get("MACHINE_0")).doubleValue());
			entry.setMaterialCost(((BigDecimal)row.get("MATERIAL_0")).doubleValue());
			entry.setTotalCost(((BigDecimal)row.get("TOTAL_0")).doubleValue());
        	costs.put(code, entry);
        }
		
		return costs;
	}

	@Override
	public void insertStandardCostsInQuickTable(List<X3StandardCostEntry> updateList,
			List<X3StandardCostEntry> insertList, String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			// FOR ATW
			jdbc = jdbc6;

			String sql = "INSERT INTO " 
							+ company + ".VSTDCST"
							+ "(ITMREF_0, TOTAL_0, MATERIAL_0, MACHINE_0, LABOUR_0, CSTDATE_0) "
							+ "VALUES (?, ?, ?, ?, ?, ?)";
			jdbc.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					X3StandardCostEntry entry = insertList.get(i);
					ps.setString(1, entry.getCode());
					ps.setDouble(2, entry.getTotalCost());
					ps.setDouble(3, entry.getMaterialCost());
					ps.setDouble(4, entry.getMachineCost());
					ps.setDouble(5, entry.getLabourCost());
					ps.setTimestamp(6, entry.getDate());
				}
				@Override
				public int getBatchSize() {
					return insertList.size();
				}
			});
			
			sql = "UPDATE " 
					+ company + ".VSTDCST SET "
					+ "ITMREF_0 = ?, "
					+ "TOTAL_0 = ?, "
					+ "MATERIAL_0 = ?, "
					+ "MACHINE_0 = ?, "
					+ "LABOUR_0 = ?, "
					+ "CSTDATE_0 = ? "
					+ "WHERE "
					+ "ITMREF_0 = ?";
			jdbc.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					X3StandardCostEntry entry = updateList.get(i);
					ps.setString(1, entry.getCode());
					ps.setDouble(2, entry.getTotalCost());
					ps.setDouble(3, entry.getMaterialCost());
					ps.setDouble(4, entry.getMachineCost());
					ps.setDouble(5, entry.getLabourCost());
					ps.setTimestamp(6, entry.getDate());
					ps.setString(7, entry.getCode());
				}
				@Override
				public int getBatchSize() {
					return updateList.size();
				}
			});
		}
		else {
			// FOR WPS
			jdbc = jdbc11;

			String sql = "INSERT INTO " 
							+ company + ".VSTDCST"
							+ "(ITMREF_0, TOTAL_0, MATERIAL_0, MACHINE_0, LABOUR_0, CSTDATE_0, CREDATTIM_0, UPDDATTIM_0, AUUID_0, CREUSR_0, UPDUSR_0) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, hextoraw(?), ?, ?)";
			jdbc.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					X3StandardCostEntry entry = insertList.get(i);
					ps.setString(1, entry.getCode());
					ps.setDouble(2, entry.getTotalCost());
					ps.setDouble(3, entry.getMaterialCost());
					ps.setDouble(4, entry.getMachineCost());
					ps.setDouble(5, entry.getLabourCost());
					ps.setTimestamp(6, entry.getDate());
					ps.setTimestamp(7, entry.getDate());
					ps.setTimestamp(8, entry.getDate());
					ps.setString(9, UUID.randomUUID().toString().replaceAll("-", ""));
					ps.setString(10, "ADMIN");
					ps.setString(11, "ADMIN");
				}
				@Override
				public int getBatchSize() {
					return insertList.size();
				}
			});
			
			sql = "UPDATE " 
					+ company + ".VSTDCST SET "
					+ "ITMREF_0 = ?, "
					+ "TOTAL_0 = ?, "
					+ "MATERIAL_0 = ?, "
					+ "MACHINE_0 = ?, "
					+ "LABOUR_0 = ?, "
					+ "CSTDATE_0 = ?, "
					+ "UPDDATTIM_0 = ? "
					+ "WHERE "
					+ "ITMREF_0 = ?";
			jdbc.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					X3StandardCostEntry entry = updateList.get(i);
					ps.setString(1, entry.getCode());
					ps.setDouble(2, entry.getTotalCost());
					ps.setDouble(3, entry.getMaterialCost());
					ps.setDouble(4, entry.getMachineCost());
					ps.setDouble(5, entry.getLabourCost());
					ps.setTimestamp(6, entry.getDate());
					ps.setTimestamp(7, entry.getDate());
					ps.setString(8, entry.getCode());
				}
				@Override
				public int getBatchSize() {
					return updateList.size();
				}
			});
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		
	}

	@Override
	public List<X3Supplier> findProductSuppliers(String company, String productCode) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList( ""
				+ "SELECT "
				+ "BPR.BPRNUM_0, "
				+ "BPR.BPRNAM_0, "
				+ "BPR.BPRNAM_1, "
				+ "BPR.CRY_0, "
				+ "TBC.EECFLG_0 "
				+ "FROM ("
				+ company + ".ITMBPS BPS INNER JOIN " + company + ".BPARTNER BPR "
				+ "ON "
				+ "BPS.BPSNUM_0 = BPR.BPRNUM_0 ) INNER JOIN " + company + ".TABCOUNTRY TBC "
				+ "ON "
				+ "BPR.CRY_0 = TBC.CRY_0 "
				+ "WHERE "
				+ "BPS.ITMREF_0 = ? "
				,
                new Object[]{productCode});
		
		List<X3Supplier> list = new ArrayList<>();
        X3Supplier supplier;
        for(Map<String,Object> row: resultSet ){
        	supplier = new X3Supplier();
        	supplier.setCode((String)row.get("BPRNUM_0"));
        	supplier.setName((String)row.get("BPRNAM_0") + " " + (String)row.get("BPRNAM_1"));
        	supplier.setCountry((String)row.get("CRY_0"));
        	supplier.setEuMember(((BigDecimal)row.get("EECFLG_0")).intValue() == 2 ? true : false);
        	
        	list.add(supplier);
        }
        
		return list;
	}

	@Override
	public X3SupplyStatInfo getSupplyStatistics(String company, String productCode, String supplierCode) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList( ""
				+ "SELECT "
				+ "PRC.RCPDAT_0, "
				+ "PRC.PTHNUM_0, "
				+ "PRC.QTYSTU_0, "
				+ "PRC.BPSNUM_0, "
				+ "PRC.ITMREF_0, "
				+ "PRC.NETPRI_0, "
				+ "ORD.CHGCOE_0 "
				+ "FROM "
				+ company + ".PRECEIPTD PRC INNER JOIN " + company + ".PORDER ORD "
				+ "ON PRC.POHNUM_0 = ORD.POHNUM_0 "
				+ "WHERE "
				+ "PRC.BPSNUM_0 = ? "
				+ "AND "
				+ "PRC.ITMREF_0 = ? "
				+ "ORDER BY PRC.RCPDAT_0 "
				,
                new Object[]{supplierCode, productCode});
		
		X3SupplyStatInfo info = new X3SupplyStatInfo();
		
        for(Map<String,Object> row: resultSet ){
        	info.setProductCode((String)row.get("ITMREF_0"));
        	info.setSupplierCode((String)row.get("BPSNUM_0"));
        	info.setLastReceptionDate((Timestamp)row.get("RCPDAT_0"));
        	info.setLastReceptionNumber((String)row.get("PTHNUM_0"));
        	info.setNumberOfReceptions(info.getNumberOfReceptions() + 1);
        	info.setPurchasedQuantity(info.getPurchasedQuantity() + ((BigDecimal)row.get("QTYSTU_0")).intValue());
        	info.setLastReceptionPrice(((BigDecimal)row.get("NETPRI_0")).doubleValue() * ((BigDecimal)row.get("CHGCOE_0")).doubleValue());
        }
		
		return info;
		
	}

	@Override
	public Map<String, Map<Integer, X3RouteLine>> getRoutesMap(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		Map<String, Map<Integer, X3RouteLine>> result = new HashMap<>();
		Map<Integer, X3RouteLine> route;

		List<Map<String,Object>> resultSet = jdbc.queryForList(
						"SELECT "
						+ "ROO.OPENUM_0, "
						+ "ROO.ITMREF_0, "
						+ "ROO.ROODES_0, "
						+ "ROO.WST_0, "
						+ "ROO.LABWST_0, "
						+ "ROO.X_SULABWST_0, "
						+ "ROO.SETTIM_0, "
						+ "ROO.WAITIM_0, "
						+ "ROO.X_SULABNR_0, "
						+ "ROO.X_SUESNR_0 "
						+ "FROM " + company + ".ROUOPE ROO "
						+ "WHERE ROO.ROUALT_0 = 1 "
						+ "ORDER BY ROO.OPENUM_0 ASC",
                new Object[]{});

		X3RouteLine line = null;
		String code;
		int ope;
		
        for(Map<String,Object> row: resultSet ){
        	// keys
        	code = (String)row.get("ITMREF_0");
        	ope = ((BigDecimal)row.get("OPENUM_0")).intValue();
        	// object
        	line = new X3RouteLine();
        	line.setOperationNumber(ope);
        	line.setCode(code);
        	line.setOperationDescription((String)row.get("ROODES_0"));
        	line.setMachine((String)row.get("WST_0"));
        	line.setCenter((String)row.get("LABWST_0"));
        	line.setSquad((String)row.get("X_SULABWST_0"));
        	line.setSetTime(((BigDecimal)row.get("SETTIM_0")).doubleValue());
        	line.setWaitTime(((BigDecimal)row.get("WAITIM_0")).doubleValue());
        	line.setPersonPreparing(((BigDecimal)row.get("X_SULABNR_0")).doubleValue());
        	line.setFrazLavorazione(((BigDecimal)row.get("X_SUESNR_0")).doubleValue());
        	
        	// assign
        	if(!result.containsKey(code)){
        		// create route map
        		route = new TreeMap<>();
        		route.put(ope,line);
        		// add route
        		result.put(code, route);
        	}
        	else{
        		route = result.get(code);
        		route.put(ope, line);
        	}
        }
		
		return result;
	}

	@Override
	public Map<String, String> getWorkcenterNumbersMapByMachines(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
						"SELECT "
						+ "WST.WST_0, "
						+ "WST.WCR_0 "
						+ "FROM " + company + ".WORKSTATIO WST",
                new Object[]{});

		
		Map<String,String> result = new HashMap<>();
		
        for(Map<String,Object> row: resultSet ){
        	result.put((String)row.get("WST_0"), (String)row.get("WCR_0"));
        }
		
		return result;
	}

	@Override
	/*
	 * key & value format: "number/line"
	 */
	public Map<String, String> getPendingProductionOrdersBySaleOrders(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
						"SELECT "
						+ "MFI.VCRNUMORI_0, "
						+ "MFI.VCRLINORI_0, "
						+ "MFI.MFGNUM_0, "
						+ "MFI.MFGLIN_0 "
						+ "FROM "
						+ company + ".MFGITM MFI "
						+ "WHERE "
						+ "MFI.ITMSTA_0 < 3 ",
                new Object[]{});

		
		Map<String,String> result = new HashMap<>();
		
        for(Map<String,Object> row: resultSet ){
        	result.put((String)row.get("VCRNUMORI_0")+"/"+((BigDecimal)row.get("VCRLINORI_0")).intValue(), (String)row.get("MFGNUM_0")+"/"+((BigDecimal)row.get("MFGLIN_0")).intValue());
        }
		
		return result;
	}

	@Override
	public Map<String, Double> getCurrentStandardCostsMap(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "VSTD.ITMREF_0, "
				+ "VSTD.TOTAL_0 "
				+ "FROM "
				+ company + ".VSTDCST VSTD"
				,
                new Object[]{}
				);
		
		Map <String, Double> map = new HashMap<>();
		for(Map<String,Object> row: resultSet ){
			map.put(((String)row.get("ITMREF_0")), ((BigDecimal)row.get("TOTAL_0")).doubleValue());
		}
			
		return map;
	}

	@Override
	public List<X3EnvironmentInfo> getEnvironmentInfoInPeriod(Date startDate, Date endDate, String type,
			String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		String query = ""
				+ "SELECT "
				+ "ITM.ITMREF_0, "
				+ "ITM.ITMDES1_0, "
				+ "STJ.IPTDAT_0, "
				+ "STJ.QTYSTU_0, "
				+ "ITM.STU_0, "
				+ "APL.LANMES_0, "
				+ "ITM.TSICOD_0, "
				+ "ITM.TSICOD_1 "
				+ "FROM "
				+ "("
				+ company +".STOJOU STJ INNER JOIN " + company +".ITMMASTER ITM "
				+ "ON STJ.ITMREF_0 = ITM.ITMREF_0"
				+ ") "
				+ "INNER JOIN " + company + ".APLSTD APL "
				+ "ON STJ.TRSTYP_0 = APL.LANNUM_0 "
				+ "WHERE " 
				+ "(APL.LANCHP_0 = ?) " 
				+ "AND (APL.LAN_0 = ?) "
				+ "AND (STJ.TRSTYP_0 = ? Or STJ.TRSTYP_0 = ?) "
				+ "AND (STJ.IPTDAT_0 > ? And STJ.IPTDAT_0 < ?) ";
		
		Object[] criteria = new Object[] {};
		switch (type) {
		case JdbcOracleX3Service.ENV_INFO_WIRE:
			// ITM.ITMREF_0 Like ? 
			// "04783%",
			query += " AND ((ITM.ITMDES1_0 Like ? OR ITM.ITMDES1_0 Like ?))";
			criteria = new Object[] {704, "POL", 2, 6, startDate, endDate, "DRUT %", "FILO %"};
			break;
		case JdbcOracleX3Service.ENV_INFO_PAINT:
			if(company.equals("ATW")) {
				query += " AND (ITM.TSICOD_1 = ?)";
				criteria = new Object[] {704, "POL", 2, 6, startDate, endDate, "FAR"};
			}
			else {
				query += " AND (ITM.ITMDES1_0 LIKE ?)";
				criteria = new Object[] {704, "POL", 2, 6, startDate, endDate, "%FARB%"};
			}
			break;
		case JdbcOracleX3Service.ENV_INFO_GAS:
			query += " AND (ITM.ITMDES1_0 Like ?)";
			criteria = new Object[] {704, "POL", 2, 6, startDate, endDate, "GAZ%"};
			break;
		case JdbcOracleX3Service.ENV_INFO_GLUE:
			if(company.equals("ATW")) {
				query += " AND (ITM.ITMDES1_0 Like ?)"; 
				criteria = new Object[] {704, "POL", 2, 6, startDate, endDate, "KLEJ %"};
			}
			else {
				query += " AND (ITM.TSICOD_1 = ?)"; 
				criteria = new Object[] {704, "POL", 2, 6, startDate, endDate, "KLE"};
			}
			break;
		default:
			throw new NotFoundException("Unknown list type: " + type);
		}
		
		query +=" ORDER BY ITM.ITMREF_0, STJ.IPTDAT_0";
		
		List<Map<String, Object>> resultSet = jdbc.queryForList(
				query, 
				criteria
				);
		
		List<X3EnvironmentInfo> list = new ArrayList<>();
		X3EnvironmentInfo einfo;
		for (Map<String, Object> row : resultSet) {
			einfo = new X3EnvironmentInfo();
			einfo.setCode((String)row.get("ITMREF_0"));
			einfo.setDescription((String)row.get("ITMDES1_0"));
			einfo.setGr1((String)row.get("TSICOD_0"));
			einfo.setGr2((String)row.get("TSICOD_1"));
			einfo.setAccDate((Timestamp)row.get("IPTDAT_0"));
			einfo.setMovementName((String)row.get("LANMES_0"));
			einfo.setQuantity(((BigDecimal)row.get("QTYSTU_0")).doubleValue());
			einfo.setUnit((String)row.get("STU_0"));
			
			list.add(einfo);
		}
		return list;
	}

	@Override
	public List<X3AvgPriceLine> getAveragePricesByInvoices(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2018);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "SID.ITMREF_0 AS code, "
				+ "ITM.ITMDES1_0 AS descr, "
				+ "ITM.TCLCOD_0 AS category, "
				+ "ITM.TSICOD_1 AS gr2, "
				+ "Sum(SID.QTYSTU_0) AS quantity, "
				+ "AVG(SID.NETPRI_0 * SIV.RATMLT_0) AS average, "
				+ "Sum(SID.NETPRI_0 * SIV.RATMLT_0 * SID.QTYSTU_0) AS w_ingr_pric "
				+ "FROM "
				+ "(" + company + ".SINVOICE SIV INNER JOIN " + company + ".SINVOICED SID "
				+ "ON SIV.NUM_0 = SID.NUM_0) "
				+ "INNER JOIN "  + company + ".ITMMASTER ITM "
				+ "ON SID.ITMREF_0 = ITM.ITMREF_0 "
				+ "WHERE "
				+ "SIV.ACCDAT_0 >= ? "
				+ "GROUP BY "
				+ "SID.ITMREF_0, "
				+ "ITM.ITMDES1_0, "
				+ "ITM.TCLCOD_0, "
				+ "ITM.TSICOD_1 "
				+ "ORDER BY SID.ITMREF_0"
				,
                new Object[]{new Timestamp(cal.getTime().getTime())});
		
		List<X3AvgPriceLine> list = new ArrayList<>();
		X3AvgPriceLine line;
        for(Map<String,Object> row: resultSet ){
        	line = new X3AvgPriceLine();
        	line.setProductCode((String)row.get("code"));
        	line.setProductDescription((String)row.get("descr"));
        	line.setCategory((String)row.get("category"));
        	line.setGr2((String)row.get("gr2"));
        	line.setQuantity(((BigDecimal)row.get("quantity")).doubleValue());
        	line.setAvgPrice(((BigDecimal)row.get("average")).doubleValue());
        	line.setWeightAvgPrice(
        			((BigDecimal)row.get("w_ingr_pric")).doubleValue()
        			/
        			((BigDecimal)row.get("quantity")).doubleValue()
        			);        	
        	
        	list.add(line);
        }
        
		return list;
	}

	@Override
	public List<X3AvgPriceLine> getAveragePricesByOrders(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2018);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "SOQ.ITMREF_0 AS code, "
				+ "ITM.ITMDES1_0 AS descr, "
				+ "ITM.TCLCOD_0 AS category, "
				+ "ITM.TSICOD_1 AS gr2, "
				+ "Sum(SOQ.QTYSTU_0) AS quantity, "
				+ "Avg(SOP.NETPRI_0 * SOR.CHGRAT_0) AS average, "
				+ "Sum(SOP.NETPRI_0 * SOR.CHGRAT_0 * SOQ.QTYSTU_0) AS w_ingr_pric "
				+ "FROM "
				+ "(" + company + ".SORDERP SOP INNER JOIN "
				+ "(" + company + ".ITMMASTER ITM INNER JOIN " + company + ".SORDERQ SOQ "
				+ "ON ITM.ITMREF_0 = SOQ.ITMREF_0) "
				+ "ON "
				+ "(SOP.SOPSEQ_0 = SOQ.SOQSEQ_0) AND "
				+ "(SOP.SOPLIN_0 = SOQ.SOPLIN_0) AND "
				+ "(SOP.SOHNUM_0 = SOQ.SOHNUM_0)) "
				+ "INNER JOIN " + company + ".SORDER SOR "
				+ "ON SOQ.SOHNUM_0 = SOR.SOHNUM_0 "
				+ "WHERE "
				+ "SOQ.DEMDLVDAT_0 >= ? "
				+ "GROUP BY "
				+ "SOQ.ITMREF_0, "
				+ "ITM.ITMDES1_0, "
				+ "ITM.TCLCOD_0, "
				+ "ITM.TSICOD_1 "
				+ "ORDER BY "
				+ "SOQ.ITMREF_0"
				,
                new Object[]{new Timestamp(cal.getTime().getTime())});
		
		List<X3AvgPriceLine> list = new ArrayList<>();
		X3AvgPriceLine line;
        for(Map<String,Object> row: resultSet ){
        	line = new X3AvgPriceLine();
        	line.setProductCode((String)row.get("code"));
        	line.setProductDescription((String)row.get("descr"));
        	line.setCategory((String)row.get("category"));
        	line.setGr2((String)row.get("gr2"));
        	line.setQuantity(((BigDecimal)row.get("quantity")).doubleValue());
        	line.setAvgPrice(((BigDecimal)row.get("average")).doubleValue());
        	line.setWeightAvgPrice(
        			((BigDecimal)row.get("w_ingr_pric")).doubleValue()
        			/
        			((BigDecimal)row.get("quantity")).doubleValue()
        			);
        	
        	
        	list.add(line);
        }
        
		return list;
	}

	@Override
	public Map<String, Integer> findStockForAllProductsWithStock(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "itv.ITMREF_0, "
				+ "itv.PHYSTO_0 "
				+ "FROM " 
				+ company + ".ITMMVT itv "
				+ "WHERE itv.PHYSTO_0 > 0"
				,
                new Object[]{}
				);
		
		Map<String, Integer> map = new HashMap<>();
		for(Map<String,Object> row: resultSet ){
			map.put((String)row.get("ITMREF_0"), ((BigDecimal)row.get("PHYSTO_0")).intValue());
		}
		return map;
	}

	@Override
	public Map<String, Double> getExpectedDeliveriesByDate(Date date, String company) {
				// ===========================================================
				// ==== TMP JDBC DUALITY =====================================
				if(this.x3v.equalsIgnoreCase("6")) {
					jdbc = jdbc6;
				}
				else {
					jdbc = jdbc11;
				}
				// ==== TMP JDBC DUALITY =====================================
				// ===========================================================
				
				List<Map<String,Object>> resultSet = jdbc.queryForList(
						"SELECT "
						+ "POQ.ITMREF_0, (POQ.QTYSTU_0- POQ.RCPQTYSTU_0) AS TO_GET "
						+ "FROM "
						+ company + ".PORDERQ POQ "
						+ "WHERE "
						+ "(POQ.QTYSTU_0 - POQ.RCPQTYSTU_0) > 0 "
						+ "AND POQ.LINCLEFLG_0 = 1 "
						+ "AND POQ.X_RCPDAT_0 <= ?"
						,
		                new Object[]{dateHelper.getTime(date)}
						);
				
				Map<String, Double> map = new HashMap<>();
				String code;
				double value;
				for(Map<String,Object> row: resultSet ){
					code = (String)row.get("ITMREF_0");
					value = ((BigDecimal)row.get("TO_GET")).doubleValue();
					if(map.containsKey(code)) {
						map.put(code, map.get(code)+value);
					}
					else {
						map.put(code, value);
					}
				}
				return map;
	}

	@Override
	public Map<String, Date> getLatestExpectedDeliveryDateForCodeByDate(Date date, String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "POQ.ITMREF_0, "
				+ "POQ.X_RCPDAT_0 "
				+ "FROM "
				+ company + ".PORDERQ POQ "
				+ "WHERE "
				+ "(POQ.QTYSTU_0 - POQ.RCPQTYSTU_0) > 0 "
				+ "AND POQ.LINCLEFLG_0 = 1 "
				+ "AND POQ.X_RCPDAT_0 <= ?"
				,
                new Object[]{dateHelper.getTime(date)}
				);
		
		Map<String, Date> map = new HashMap<>();
		String code;
		Date tmpDate;
		
		for(Map<String,Object> row: resultSet ){
			code = (String)row.get("ITMREF_0");
			tmpDate = new Date(((Timestamp)row.get("X_RCPDAT_0")).getTime());
			
			if(map.containsKey(code)) {
				if(tmpDate.after(map.get(code))) {
					map.put(code, tmpDate);
				}
			}
			else {
				map.put(code, tmpDate);
			}
		}
		return map;
	}
	
	@Override
	public Map<String, X3DeliverySimpleInfo> getFirstUpcomingDeliveriesMapByCodeAfterDate(Date date, String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT * FROM ( " 
					+ "SELECT "
					+ "POQ.POHNUM_0, " 
					+ "POQ.POPLIN_0, " 
					+ "POQ.ITMREF_0, " 
					+ "POQ.QTYSTU_0, " 
					+ "POQ.RCPQTYSTU_0, " 
					+ "BPR.BPRNUM_0, "
					+ "BPR.BPRNAM_0, " 
					+ "BPR.CRY_0, "
					+ "POQ.X_RCPDAT_0, " 
					+ "POQ.ORDDAT_0, " 
					+ "(POQ.QTYSTU_0 - POQ.RCPQTYSTU_0) AS TO_GET, "
					+ "ROW_NUMBER() OVER (PARTITION BY POQ.ITMREF_0 ORDER BY POQ.X_RCPDAT_0 ASC) AS ROWNUMBER "
					+ "FROM " + company + ".PORDERQ POQ INNER JOIN "  + company + ".BPARTNER BPR ON POQ.BPSNUM_0 = BPR.BPRNUM_0 "
					+ "WHERE POQ.LINCLEFLG_0 = 1  AND POQ.X_RCPDAT_0 >= ? AND (POQ.QTYSTU_0 - POQ.RCPQTYSTU_0) > 0 "
				+ ")  WHERE ROWNUMBER = 1 "
				,
                new Object[]{dateHelper.getTime(date)}
				);
		
		Map<String, X3DeliverySimpleInfo> map = new HashMap<>();
		X3DeliverySimpleInfo info;
		for(Map<String,Object> row: resultSet ){
			info = new X3DeliverySimpleInfo();
			info.setDocumentNr((String)row.get("POHNUM_0"));
			info.setDocumentLine(((BigDecimal)row.get("POPLIN_0")).intValue()+ "");
			info.setProductCode((String)row.get("ITMREF_0"));
			info.setQuantityOrdered(((BigDecimal)row.get("QTYSTU_0")).intValue());
			info.setQuantityReceived(((BigDecimal)row.get("RCPQTYSTU_0")).intValue());
			info.setQuantityLeftToGet(((BigDecimal)row.get("TO_GET")).intValue());
			info.setSupplierCode((String)row.get("BPRNUM_0"));
			info.setSupplierName((String)row.get("BPRNAM_0"));
			info.setCountry((String)row.get("CRY_0"));
			info.setDate((Timestamp)row.get("X_RCPDAT_0"));
			info.setOrderDate((Timestamp)row.get("ORDDAT_0"));
			map.put(info.getProductCode(), info); 
		}
		return map;
	}

	@Override
	public Map<String, X3DeliverySimpleInfo> getMostRecentDeliveriesMapByCodeBeforeDate(Date date, String company) {
				// ===========================================================
				// ==== TMP JDBC DUALITY =====================================
				if(this.x3v.equalsIgnoreCase("6")) {
					jdbc = jdbc6;
				}
				else {
					jdbc = jdbc11;
				}
				// ==== TMP JDBC DUALITY =====================================
				// ===========================================================
				
				List<Map<String,Object>> resultSet = jdbc.queryForList(
						"SELECT * FROM ( "
							+ "SELECT "
							+ "PRD.PTHNUM_0, "
							+ "PRD.PTDLIN_0, "
							+ "PRD.ITMREF_0, "
							+ "PRD.QTYSTU_0, "
							+ "BPR.BPRNUM_0, "
							+ "BPR.BPRNAM_0, "
							+ "BPR.CRY_0, "
							+ "PRD.RCPDAT_0, "
							+ "ROW_NUMBER() OVER (PARTITION BY PRD.ITMREF_0 ORDER BY PRD.RCPDAT_0 DESC) AS ROWNUMBER "
							+ "FROM "
							+ company + ".PRECEIPTD PRD INNER JOIN " + company + ".BPARTNER BPR ON PRD.BPSNUM_0 = BPR.BPRNUM_0 "
							+ "WHERE PRD.RCPDAT_0 <= ? "
						+ ")  WHERE ROWNUMBER = 1 "
						,
		                new Object[]{dateHelper.getTime(date)}
						);
				
				Map<String, X3DeliverySimpleInfo> map = new HashMap<>();
				X3DeliverySimpleInfo info;
				for(Map<String,Object> row: resultSet ){
					info = new X3DeliverySimpleInfo();
					info.setDocumentNr((String)row.get("PTHNUM_0"));
					info.setDocumentLine(((BigDecimal)row.get("PTDLIN_0")).intValue()+ "");
					info.setProductCode((String)row.get("ITMREF_0"));
					info.setQuantityOrdered(((BigDecimal)row.get("QTYSTU_0")).intValue());
					info.setQuantityReceived(((BigDecimal)row.get("QTYSTU_0")).intValue());
					info.setQuantityLeftToGet(0);
					info.setSupplierCode((String)row.get("BPRNUM_0"));
					info.setSupplierName((String)row.get("BPRNAM_0"));
					info.setCountry((String)row.get("CRY_0"));
					info.setDate((Timestamp)row.get("RCPDAT_0"));
					map.put(info.getProductCode(), info);
				}
				return map;
	}

	@Override
	public Map<String, Integer> findProductsInReplenish(String company) {
				// ===========================================================
				// ==== TMP JDBC DUALITY =====================================
				if(this.x3v.equalsIgnoreCase("6")) {
					jdbc = jdbc6;
				}
				else {
					jdbc = jdbc11;
				}
				// ==== TMP JDBC DUALITY =====================================
				// ===========================================================
				
				List<Map<String,Object>> resultSet = jdbc.queryForList( ""
						+ "SELECT "
						+ "ITV.ITMREF_0, "
						+ "ITV.ORDSTO_0 "
						+ "FROM "
						+ company + ".ITMMVT ITV "
						+ "WHERE"
						+ " ITV.ORDSTO_0 > ? "
						,
		                new Object[]{0});
				
		        Map <String, Integer> map = new HashMap<>();
		        for(Map<String,Object> row: resultSet ){
		        	map.put((String)row.get("ITMREF_0"), ((BigDecimal)row.get("ORDSTO_0")).intValue());
		        }
		        
		        return map;
	}

	@Override
	public Map<String, X3ProductEventsHistory> getAcvProductsEventsHistory(Date startDate, Date endDate,
			List<X3ConsumptionProductInfo> acvInfo, String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		
		Map<String, X3ProductEventsHistory> historyMap = new HashMap<>();
		X3ProductEventsHistory history;
		X3ProductEvent event;
		X3ProductEvent tmpEvent;
		
		
		
		// create history objects list for all ACV codes with INIT event today (adjustment = 0, before = after)
		for(X3ConsumptionProductInfo info: acvInfo) {
			history = new X3ProductEventsHistory(info.getProductCode());
			event = new X3ProductEvent();
			event.setProductCode(info.getProductCode());
			event.setDate(new java.util.Date());
			event.setAdjustment(0);
			event.setBefore(info.getStock());
			event.setAfter(info.getStock());
			event.setTransactionType(0);
			history.addEvent(event);
			historyMap.put(history.getProductCode(), history);
		}
		
		// query
		String query = ""
				+ "SELECT "
				+ "STJ.ITMREF_0, "
				+ "STJ.IPTDAT_0, "
				+ "STJ.IPTDAT_0, "
				+ "STJ.QTYSTU_0, "
				+ "STJ.TRSTYP_0 "
				+ "FROM " + company + ".STOJOU STJ INNER JOIN " + company + ".ITMMASTER ITM "
				+ "ON STJ.ITMREF_0 = ITM.ITMREF_0 "
				+ "WHERE "
				+ "STJ.IPTDAT_0 >= ? "
				+ "AND STJ.TRSTYP_0  IN (1, 2, 3, 4, 5, 6, 12) "
				+ "AND ITM.TCLCOD_0 = ? "
				+ "AND STJ.REGFLG_0 != 2 "
				+ "ORDER BY STJ.IPTDAT_0 DESC, STJ.CRETIM_0 DESC";
		
		
		List<Map<String,Object>> resultSet = jdbc.queryForList( 
				query,
                new Object[]{dateHelper.getTime(startDate), "ACV"});
		
		// read all events from database (descending) and put into map
		// since present day back to startDate
        for(Map<String,Object> row: resultSet ){
        	if(!historyMap.containsKey((String)row.get("ITMREF_0"))) {
        		continue;
        	}
        	history = historyMap.get((String)row.get("ITMREF_0"));
        	// tmp = previous event in list (more recent)
        	tmpEvent = history.getEvents().get(history.getEvents().size()-1);
        	event = new X3ProductEvent();
			event.setProductCode(tmpEvent.getProductCode());
			event.setDate((Timestamp)row.get("IPTDAT_0"));
			event.setAdjustment(((BigDecimal)row.get("QTYSTU_0")).doubleValue());
			event.setBefore(tmpEvent.getBefore() + (-1)*event.getAdjustment());
			event.setAfter(tmpEvent.getBefore());
			event.setTransactionType(((BigDecimal)row.get("TRSTYP_0")).intValue());
			history.addEvent(event);
        }
        
        
        // for all history objects add init event at startDate
        for(X3ProductEventsHistory hist: historyMap.values()) {
        	// tmp = first event found for code after startDate (last object in descending list from DB)
        	// event = created init event to act as startDate state
        	tmpEvent = hist.getEvents().get(hist.getEvents().size()-1);
        	event = new X3ProductEvent();
			event.setProductCode(tmpEvent.getProductCode());
			event.setDate(startDate);
			event.setAdjustment(0);
			event.setBefore(tmpEvent.getBefore());
			event.setAfter(tmpEvent.getBefore());
			event.setTransactionType(0);
			hist.addEvent(event);
        }
        
        
        // reverse events in each history object and cut those after 
        List<X3ProductEvent> rev;
        for(X3ProductEventsHistory hist: historyMap.values()) {
        	// never empty getEvents() - always at least init event
        	tmpEvent = hist.getEvents().get(0);
        	rev = new ArrayList<>();
        	for(int i = hist.getEvents().size()-1 ; i >= 0; i-- ) {
        		if(hist.getEvents().get(i).getDate().before(endDate)) {
        			rev.add(hist.getEvents().get(i));
        			tmpEvent = hist.getEvents().get(i);
        		}
        	}
        	// swap history list to rev
        	hist.setEvents(rev);
        	// set last event
        	// tmp = last event before endDate
        	// event = created last event to act as endDate state
        	event = new X3ProductEvent();
			event.setProductCode(tmpEvent.getProductCode());
			event.setDate(endDate);
			event.setAdjustment(0);
			event.setBefore(tmpEvent.getBefore());
			event.setAfter(tmpEvent.getBefore());
			event.setTransactionType(99);
			hist.addEvent(event);
        }
		
		return historyMap;
	}

	@Override
	public List<NoBomCodeInfo> getNoBomCodesListIncompleteObjects(String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList( ""
				+ "SELECT "
				+ "ITM.ITMREF_0, "
				+ "ITM.ITMDES1_0, "
				+ "ITM.TSICOD_0, "
				+ "ITM.TSICOD_1, "
				+ "ITM.TCLCOD_0, "
				+ "ITV.PHYSTO_0, "
				+ "ITM.CREDAT_0, "
				+ "ITV.LASRCPDAT_0, "
				+ "ITV.LASISSDAT_0, "
				+ "BOD.CPNITMREF_0 "
				+ "FROM "
				+ "(" + company + ".ITMMASTER ITM "
				+ "INNER JOIN " + company + ".ITMMVT ITV "
				+ "ON ITM.ITMREF_0 = ITV.ITMREF_0) "
				+ "LEFT JOIN " + company + ".BOMD BOD "
				+ "ON ITV.ITMREF_0 = BOD.CPNITMREF_0 "
				+ "WHERE ITM.ITMSTA_0 = 1  AND BOD.CPNITMREF_0 IS NULL"
				,
                new Object[]{});
		
		List<NoBomCodeInfo> list = new ArrayList<>();
        NoBomCodeInfo inf;
        for(Map<String,Object> row: resultSet ){
        	inf = new NoBomCodeInfo();
        	inf.setCode((String)row.get("ITMREF_0"));
        	inf.setCategory((String)row.get("TCLCOD_0"));
        	inf.setCreationDate((Timestamp)row.get("CREDAT_0"));
        	inf.setDescription((String)row.get("ITMDES1_0"));
        	inf.setGr1((String)row.get("TSICOD_0"));
        	inf.setGr2((String)row.get("TSICOD_1"));
        	inf.setLastIssueDate((Timestamp)row.get("LASISSDAT_0"));
        	inf.setLastReceptionDate((Timestamp)row.get("LASRCPDAT_0"));
        	inf.setStockX3(((BigDecimal)row.get("PHYSTO_0")).intValue());

        	list.add(inf);
        }
        
        return list;
	}

	@Override
	public Map<String, X3SaleInfo> getSaleInfoInPeriod(Date startDate, Date endDate, String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		List<Map<String,Object>> resultSet = jdbc.queryForList( ""
				+ "SELECT "
				+ "SID.ITMREF_0, "
				+ "Sum(SID.NETPRI_0 * SIH.RATMLT_0 * SID.QTY_0) AS VALUE_PLN, "
				+ "Sum(SID.QTY_0) AS QUANTITY "
				+ "FROM "
				+ company + ".SINVOICED SID INNER JOIN " + company + ".SINVOICE SIH "
				+ "ON SID.NUM_0 = SIH.NUM_0 "
				+ "WHERE "
				+ "SID.INVDAT_0 >= ? "
				+ "AND "
				+ "SID.INVDAT_0 <= ? "
				+ "GROUP BY SID.ITMREF_0"
				,
                new Object[]{startDate, endDate});
		
		Map<String, X3SaleInfo> map = new HashMap<>();
        X3SaleInfo inf;
        
        for(Map<String,Object> row: resultSet ){
        	inf = new X3SaleInfo();
        	inf.setCode((String)row.get("ITMREF_0"));
        	inf.setValuePln(((BigDecimal)row.get("VALUE_PLN")).doubleValue());
        	inf.setQuantity(((BigDecimal)row.get("QUANTITY")).intValue());
        	
        	map.put(inf.getCode(), inf);
        }
        
        return map;
	}

	@Override
	public NoBomCodeInfo getNoBomCodeIncompleteObject(String code, String company) {
				// ===========================================================
				// ==== TMP JDBC DUALITY =====================================
				if(this.x3v.equalsIgnoreCase("6")) {
					jdbc = jdbc6;
				}
				else {
					jdbc = jdbc11;
				}
				// ==== TMP JDBC DUALITY =====================================
				// ===========================================================
				
				List<Map<String,Object>> resultSet = jdbc.queryForList( ""
						+ "SELECT "
						+ "ITM.ITMREF_0, "
						+ "ITM.ITMDES1_0, "
						+ "ITM.TSICOD_0, "
						+ "ITM.TSICOD_1, "
						+ "ITM.TCLCOD_0, "
						+ "ITV.PHYSTO_0, "
						+ "ITM.CREDAT_0, "
						+ "ITV.LASRCPDAT_0, "
						+ "ITV.LASISSDAT_0 "
						+ "FROM "
						+ company + ".ITMMASTER ITM "
						+ "INNER JOIN " + company + ".ITMMVT ITV "
						+ "ON ITM.ITMREF_0 = ITV.ITMREF_0 "
						+ "WHERE ITM.ITMSTA_0 = 1 AND ITM.ITMREF_0 = ?"
						,
		                new Object[]{code.toUpperCase()});
				
		        NoBomCodeInfo inf = null;
		        for(Map<String,Object> row: resultSet ){
		        	inf = new NoBomCodeInfo();
		        	inf.setCode((String)row.get("ITMREF_0"));
		        	inf.setCategory((String)row.get("TCLCOD_0"));
		        	inf.setCreationDate((Timestamp)row.get("CREDAT_0"));
		        	inf.setDescription((String)row.get("ITMDES1_0"));
		        	inf.setGr1((String)row.get("TSICOD_0"));
		        	inf.setGr2((String)row.get("TSICOD_1"));
		        	inf.setLastIssueDate((Timestamp)row.get("LASISSDAT_0"));
		        	inf.setLastReceptionDate((Timestamp)row.get("LASRCPDAT_0"));
		        	inf.setStockX3(((BigDecimal)row.get("PHYSTO_0")).intValue());
		        }
		        
		        return inf;
	}

	@Override
	public List<X3HistockRawEntry> getHistockRawEntries(int years, String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		Calendar startPoint = Calendar.getInstance();
		startPoint.add(Calendar.YEAR, -years);
		
		List<Map<String,Object>> resultSet = jdbc.queryForList( ""
				+ "SELECT "
				+ "POQ.POHNUM_0 AS porder, "
				+ "POQ.POPLIN_0 AS line, "
				+ "POQ.ITMREF_0 AS code, "
				+ "ITM.TSICOD_0 AS gr1, "
				+ "ITM.TSICOD_1 AS gr2, "
				+ "ITM.TCLCOD_0 AS cat, "
				+ "POQ.QTYSTU_0 AS qordered, "
				+ "SUM(PRD.QTYSTU_0) AS qreceived, "
				+ "POQ.ORDDAT_0 AS orddat, "
				+ "MAX(PRD.RCPDAT_0) AS last_rcpdat, "
				+ "COUNT(PRD.PTHNUM_0) AS rcp_counter "
			+ "FROM "
				+ "("
				+ company + ".PRECEIPTD PRD INNER JOIN " + company + ".PORDERQ POQ "
				+ "ON (PRD.POQSEQ_0 = POQ.POQSEQ_0) "
				+ "AND (PRD.POPLIN_0 = POQ.POPLIN_0) "
				+ "AND (PRD.POHNUM_0 = POQ.POHNUM_0) "
				+ ") INNER JOIN " + company + ".ITMMASTER ITM "
				+ "ON PRD.ITMREF_0 = ITM.ITMREF_0 "
			+ "GROUP BY "
				+ "POQ.POHNUM_0, "
				+ "POQ.POPLIN_0, "
				+ "POQ.ITMREF_0, "
				+ "ITM.TSICOD_0, "
				+ "ITM.TSICOD_1, "
				+ "ITM.TCLCOD_0, "
				+ "POQ.QTYSTU_0, "
				+ "POQ.ORDDAT_0, "
				+ "POQ.LINCLEFLG_0 "
			+ "HAVING "
				+ "POQ.ORDDAT_0 >= ? "
				+ "AND "
				+ "POQ.LINCLEFLG_0 = ? "
			+ "ORDER BY "
				+ "POQ.ORDDAT_0 DESC "
				,
                new Object[]{startPoint.getTime(), 2});
		List<X3HistockRawEntry> list = new ArrayList<>();
		X3HistockRawEntry line;
        for(Map<String,Object> row: resultSet ){
        	line = new X3HistockRawEntry();
        	line.setOrder((String)row.get("porder"));
        	line.setLine(((BigDecimal)row.get("line")).intValue());
        	line.setCode((String)row.get("code"));
        	line.setGr1((String)row.get("gr1"));
        	line.setGr2((String)row.get("gr2"));
        	line.setCategory((String)row.get("cat"));
        	line.setQuantityOrdered(((BigDecimal)row.get("qordered")).intValue());
        	line.setQuantityReceived(((BigDecimal)row.get("qreceived")).intValue());
        	line.setOrderDate((Timestamp)row.get("orddat"));
        	line.setLastReceptionDate((Timestamp)row.get("last_rcpdat"));
        	line.setReceptionsCounter(((BigDecimal)row.get("rcp_counter")).intValue());

        	list.add(line);
        	
        }
        
        return list;
		
	}
	
	@Override
	public List<X3HistockRawEntry> getHistockRawPendingEntries(int years, String company) {
		// ===========================================================
				// ==== TMP JDBC DUALITY =====================================
				if(this.x3v.equalsIgnoreCase("6")) {
					jdbc = jdbc6;
				}
				else {
					jdbc = jdbc11;
				}
				// ==== TMP JDBC DUALITY =====================================
				// ===========================================================
				
				Calendar startPoint = Calendar.getInstance();
				startPoint.add(Calendar.YEAR, -years);
				
				List<Map<String,Object>> resultSet = jdbc.queryForList( ""
						+ "SELECT "
						+ "POQ.POHNUM_0 AS porder, "
						+ "POQ.POPLIN_0 AS line, "
						+ "POQ.ITMREF_0 AS code, "
						+ "ITM.TSICOD_0 AS gr1, "
						+ "ITM.TSICOD_1 AS gr2, "
						+ "ITM.TCLCOD_0 AS cat, "
						+ "POQ.QTYSTU_0 AS qordered, "
						+ "POQ.ORDDAT_0 AS orddat "
					+ "FROM "
						+ company + ".PORDERQ POQ INNER JOIN " + company + ".ITMMASTER ITM "
						+ "ON POQ.ITMREF_0 = ITM.ITMREF_0 "
						+ "WHERE "
						+ "POQ.ORDDAT_0 >= ? "
						+ "AND "
						+ "POQ.LINCLEFLG_0 != ? "
					+ "ORDER BY "
						+ "POQ.ORDDAT_0 DESC "
						,
		                new Object[]{startPoint.getTime(), 2});
				List<X3HistockRawEntry> list = new ArrayList<>();
				X3HistockRawEntry line;
		        for(Map<String,Object> row: resultSet ){
		        	line = new X3HistockRawEntry();
		        	line.setOrder((String)row.get("porder"));
		        	line.setLine(((BigDecimal)row.get("line")).intValue());
		        	line.setCode((String)row.get("code"));
		        	line.setGr1((String)row.get("gr1"));
		        	line.setGr2((String)row.get("gr2"));
		        	line.setCategory((String)row.get("cat"));
		        	line.setQuantityOrdered(((BigDecimal)row.get("qordered")).intValue());
		        	line.setQuantityReceived(0);
		        	line.setOrderDate((Timestamp)row.get("orddat"));
		        	line.setLastReceptionDate(new Timestamp(new java.util.Date().getTime()));
		        	line.setReceptionsCounter(0);

		        	list.add(line);
		        	
		        }
		        
		        return list;
				
	}

	@Override
	public void updateAverageDeliveryDaysInDatabase(List<StringIntPair> list, String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
		String sql = ""
				+ "UPDATE " 
				+ company + ".ITMFACILIT SET "
				+ "YLTACQ_0 = ? "
				+ "WHERE "
				+ "ITMREF_0 = ?";
		jdbc.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				StringIntPair entry = list.get(i);
				ps.setInt(1, entry.getIntValue());
				ps.setString(2, entry.getStringValue());
			}
			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
		
		
		
	}

	@Override
	public List<String> getComponentSuppliers(String component, String company) {
		// ===========================================================
		// ==== TMP JDBC DUALITY =====================================
		if(this.x3v.equalsIgnoreCase("6")) {
			jdbc = jdbc6;
		}
		else {
			jdbc = jdbc11;
		}
		// ==== TMP JDBC DUALITY =====================================
		// ===========================================================
		
        List<Map<String,Object>> resultSet = jdbc.queryForList(""
        		+ "SELECT "
        		+ "BPR.BPRNUM_0, "
        		+ "BPR.BPRNAM_0, "
        		+ "BPR.CRY_0 "
        		+ "FROM "
        		+ company + ".ITMBPS IBS INNER JOIN " + company + ".BPARTNER BPR "
        		+ "ON "
        		+ "IBS.BPSNUM_0 = BPR.BPRNUM_0 "
        		+ "WHERE "
        		+ "IBS.ITMREF_0 = ? "
,
                new Object[]{component});
        
        List<String> result = new ArrayList<>();
        for(Map<String,Object> row: resultSet ){
        	result.add(
        			(String)row.get("BPRNUM_0") + " - " +
        			(String)row.get("BPRNAM_0") + " [" +
        			(String)row.get("CRY_0") + "]"
        			);
        }
        
		return result;
	}

	
}

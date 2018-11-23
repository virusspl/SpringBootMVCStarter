package sbs.repository.x3;

import java.math.BigDecimal;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import sbs.controller.dirrcpship.DirectReceptionsShipmentLine;
import sbs.helpers.DateHelper;
import sbs.model.proprog.Project;
import sbs.model.wpslook.WpslookRow;
import sbs.model.x3.X3BomItem;
import sbs.model.x3.X3Client;
import sbs.model.x3.X3ConsumptionProductInfo;
import sbs.model.x3.X3ConsumptionSupplyInfo;
import sbs.model.x3.X3CoverageData;
import sbs.model.x3.X3Product;
import sbs.model.x3.X3ProductFinalMachine;
import sbs.model.x3.X3ProductSellDemand;
import sbs.model.x3.X3ProductionOrderDetails;
import sbs.model.x3.X3PurchaseOrder;
import sbs.model.x3.X3SalesOrder;
import sbs.model.x3.X3SalesOrderLine;
import sbs.model.x3.X3ShipmentMovement;
import sbs.model.x3.X3ShipmentStockLineWithPrice;
import sbs.model.x3.X3Supplier;
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

	 @Autowired 
	 @Qualifier("oracleX3JdbcTemplate") 
	 protected JdbcTemplate jdbc;
	 @Autowired
	 private DateHelper dateHelper; 
	 
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
	public X3PurchaseOrder findPurchaseOrderByNumber(String company, String number) {

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

	@Override
	public X3ProductionOrderDetails getProductionOrderInfoByNumber(String company, String number) {
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT " 
				+ company + ".MFGITM.MFGNUM_0, "
				+ company + ".MFGITM.ITMREF_0, "
				+ company + ".MFGITM.EXTQTY_0, "
				+ company + ".ITMMASTER.ITMDES1_0, "
				+ company + ".ITMMASTER.ITMDES2_0, "
				+ company + ".SORDER.SOHNUM_0, "
				+ company + ".MFGITM.BPCNUM_0, "
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
				+ company + ".MFGITM.BPCNUM_0 = " + company + ".BPARTNER.BPRNUM_0 "
				+ "WHERE UPPER("
				+ company + ".MFGITM.MFGNUM_0) = ? ",
        new Object[]{number.toUpperCase()});

		X3ProductionOrderDetails order = null;
	
		for(Map<String,Object> row: resultSet ){
			order = new X3ProductionOrderDetails();
			order.setProductionOrderNumber((String)row.get("MFGNUM_0"));
			order.setClientCode((String)row.get("BPCNUM_0"));
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
/*
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ company + ".YMANUPREVE.YCESPITE_0, "
				+ company + ".YMANUPREVE.YCESPDESCR_0, "
				+ company + ".YMANUPREVE.YFLAG_0  "
				+ "FROM "
				+ company + ".YMANUPREVE "
				,
                new Object[]{});
*/        

		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
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
				+ "ON ym.YCESPITE_0 = fx.AASDES2_0"
				,
                new Object[]{});
        
        Map <String, X3UtrMachine> map = new HashMap<>();
        X3UtrMachine machine;
        for(Map<String,Object> row: resultSet ){
        	machine = new X3UtrMachine();
        	machine.setCode((String)row.get("YCESPITE_0"));
        	machine.setName((String)row.get("YCESPDESCR_0"));
        	machine.setCodeNicim((String)row.get("NICCOD_0"));
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
	public Map<String, X3UtrFault> findUtrFaultsInPeriod(Date startDate, Date endDate) {
		String company = "ATW";
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
	public List<X3UtrFaultLine> findUtrFaultLinesAfterDate(Date startDate) {
		String company = "ATW";
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
	public Map<String, Integer> findAcvShipStock(String company) {
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
	public List<X3ShipmentStockLineWithPrice> findAllShipStockWithAveragePrice(String company) {
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
	public Map<String, Integer> getAcvConsumptionListForYear(int year, String company) {
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
	}

	@Override
	public Map<String, Integer> getAcvDemandList(String company) {
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
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "itm.ITMREF_0, "
				+ "itm.ITMDES1_0, "
				+ "itv.PHYSTO_0, "
				+ "itv.ORDSTO_0, "
				+ "itv.AVC_0 "
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

			list.add(info);
		}
			
		return list;
	}

	@Override
	public List<X3SalesOrderLine> findOpenedSalesOrderLinesInPeriod(Date startDate, Date endDate, String company) {
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "SOQ.SOHNUM_0, "
				+ "SOQ.SOPLIN_0, "
				+ "SOQ.QTYSTU_0, "
				+ "(SOQ.QTYSTU_0 - SOQ.ODLQTYSTU_0 - SOQ.DLVQTY_0) AS LEFT_TO_SEND,  "
				+ "ITM.ITMREF_0, "
				+ "ITM.ITMDES1_0, "
				+ "ITM.TSICOD_0, "
				+ "ITM.TSICOD_1, "
				+ "ITM.TSICOD_2, "
				+ "SOQ.DEMDLVDAT_0, "
				+ "BPR.BPRNUM_0, "
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
			line.setProductGr1((String)row.get("TSICOD_0"));
			line.setProductGr2((String)row.get("TSICOD_1"));
			line.setProductGr3((String)row.get("TSICOD_2"));
			line.setClientCode((String)row.get("BPRNUM_0"));
			line.setClientName(((String)row.get("BPRNAM_0")));
			line.setCountry(((String)row.get("CRY_0")));
			line.setDemandedDate(((Timestamp)row.get("DEMDLVDAT_0")));
			line.setQuantityLeftToSend(((BigDecimal)row.get("LEFT_TO_SEND")).intValue());
			line.setQuantityOrdered(((BigDecimal)row.get("QTYSTU_0")).intValue());
			list.add(line);
		}
		return list;
	}

	@Override
	public Map<String, Integer> findGeneralStockForAllProducts(String company) {

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
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(


						"SELECT " 
						+ company + ".WORKSTATIO.WST_0, "
						+ company + ".WORKSTATIO.WSTDES_0 "
						+ "FROM " 
						+ company + ".WORKSTATIO "
						+ "WHERE UPPER("
						+ company + ".WORKSTATIO.WST_0) = ?"
						+ "",
                new Object[]{code.toUpperCase()});
        		
		X3Workstation workstation = null;
		
        for(Map<String,Object> row: resultSet ){
        	workstation = new X3Workstation();
        	workstation.setCode((String)row.get("WST_0"));
        	workstation.setName(((String)row.get("WSTDES_0")));
        }
		
		return workstation;
	}

	@Override
	public boolean checkIfLocationExist(String company, String location) {
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
}

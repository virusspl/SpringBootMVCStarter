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

import sbs.helpers.DateHelper;
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
				+ company + ".YCHGSTKGX.ITMREF_0, "
				+ company + ".ITMMASTER.ITMDES1_0, "
				+ company + ".ITMMASTER.TCLCOD_0, "
				+ company + ".YCHGSTKGX.QTYSTU_0, "
				+ company + ".ITMMVT.AVC_0 "
				+ "FROM "
				+ company + ".ITMMVT "
				+ "INNER JOIN (" + company + ".YCHGSTKGX INNER JOIN " + company + ".ITMMASTER "
				+ "ON "
				+ company + ".YCHGSTKGX.ITMREF_0 = " + company + ".ITMMASTER.ITMREF_0) "
				+ "ON "
				+ company + ".ITMMVT.ITMREF_0 = " + company + ".YCHGSTKGX.ITMREF_0 "
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
				+ company + ".YCHGSTKGX.YVCRTYP_0 > 0 "
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
		
        for(Map<String,Object> row: resultSet ){
        	item = new X3ShipmentMovement();
        	item.setItemCode((String)row.get("ITMREF_0"));
        	item.setItemDescription(((String)row.get("ITMDES1_0")));
        	item.setItemCategory(((String)row.get("TCLCOD_0")));
        	item.setQuantity(((BigDecimal)row.get("QTYSTU_0")).doubleValue());
        	item.setEmergencyAveragePrice(((BigDecimal)row.get("AVC_0")).doubleValue());
        	item.setDate((Timestamp)row.get("CREDAT_0"));
        	
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

}

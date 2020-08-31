package sbs.repository.geode;

import java.math.BigDecimal;
import java.sql.Timestamp;
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

import sbs.model.geode.GeodeMovement;
import sbs.model.geode.GeodeObject;
import sbs.model.geode.GeodeQuantityObject;

@Repository
public class JdbcOracleGeodeRepositoryImpl implements JdbcOracleGeodeRepository {

	 @Autowired 
	 @Qualifier("oracleGeodeJdbcTemplate") 
	 protected JdbcTemplate jdbc;

	
	public List<Map<String,Object>> findLocationsOfProduct(String product){
		String query = "SELECT "
                + "GEOATW.SLOT.STO_0, "
                + "GEOATW.SLOT.ADD_0, "
                + "GEOATW.STOCKOBJ.SKONUM_0, "
                + "GEOATW.STOCKOBJ.ITM_0, "
                + "GEOATW.STOCKOBJ.CSUQTY_0, "
                + "GEOATW.STOCKOBJ.INPDAT_0, "
                + "GEOATW.ITEM.STU_0 "
                + "FROM "
                + "(GEOATW.SLOT INNER JOIN GEOATW.STOCKOBJ "
                + "ON (GEOATW.SLOT.ADD_0 = GEOATW.STOCKOBJ.ADD_0) "
                + "AND (GEOATW.SLOT.STO_0 = GEOATW.STOCKOBJ.STO_0)) "
                + "INNER JOIN GEOATW.ITEM "
                + "ON GEOATW.STOCKOBJ.ITM_0 = GEOATW.ITEM.ITM_0 "
                + "WHERE (UPPER(GEOATW.STOCKOBJ.ITM_0) = ?) "
                + "AND GEOATW.SLOT.STO_0 != ? "
                + "ORDER BY "
                + "GEOATW.SLOT.STO_0, GEOATW.SLOT.ADD_0";
		
		return jdbc.queryForList(query, new Object[]{product.toUpperCase(), "TMP"});
	}


	@Override
	public List<Map<String, Object>> findAllLocationsOfProducts() {
		String query = "SELECT "
                + "GEOATW.SLOT.STO_0, "
                + "GEOATW.SLOT.ADD_0, "
                + "GEOATW.STOCKOBJ.SKONUM_0, "
                + "GEOATW.STOCKOBJ.ITM_0, "
                + "GEOATW.STOCKOBJ.CSUQTY_0, "
                + "GEOATW.STOCKOBJ.INPDAT_0, "
                + "GEOATW.ITEM.STU_0 "
                + "FROM "
                + "(GEOATW.SLOT INNER JOIN GEOATW.STOCKOBJ "
                + "ON (GEOATW.SLOT.ADD_0 = GEOATW.STOCKOBJ.ADD_0) "
                + "AND (GEOATW.SLOT.STO_0 = GEOATW.STOCKOBJ.STO_0)) "
                + "INNER JOIN GEOATW.ITEM "
                + "ON GEOATW.STOCKOBJ.ITM_0 = GEOATW.ITEM.ITM_0 "
                + "WHERE GEOATW.STOCKOBJ.ITM_0 != 'TMP' "
                + "ORDER BY "
                + "GEOATW.SLOT.STO_0, GEOATW.SLOT.ADD_0";
		
		return jdbc.queryForList(query, new Object[]{});
	}

	
	@Override
	public List<GeodeMovement> findRcpMovementsInPeriod(Date startDate, Date endDate) {
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "GEOATW.ADJUSTM.ADJNUM_0, "
				+ "GEOATW.ADJUSTM.ITM_0, "
				+ "GEOATW.ADJUSTM.CUQ_0, "
				+ "GEOATW.ADJUSTM.CUQ_1, "
				+ "GEOATW.ADJUSTM.DAT_0, "
				+ "GEOATW.ADJUSTM.TIM_0, "
				+ "GEOATW.ADJUSTM.MVT_0, "
				+ "GEOATW.ADJUSTM.STO_0, "
				+ "GEOATW.ADJUSTM.SLO_0, "
				+ "GEOATW.ADJUSTM.CREUSR_0, "
				+ "GEOATW.AUTILIS.NOMUSR_0 "
				+ "FROM "
				+ "GEOATW.ADJUSTM INNER JOIN GEOATW.AUTILIS "
				+ "ON "
				+ "GEOATW.ADJUSTM.CREUSR_0 = GEOATW.AUTILIS.USR_0 "
				+ "WHERE "
				+ "GEOATW.ADJUSTM.DAT_0 >= ? AND "
				+ "GEOATW.ADJUSTM.DAT_0 <= ? AND "
				+ "("
				+ "GEOATW.ADJUSTM.CREUSR_0 LIKE 'MPR%' OR "
				+ "GEOATW.ADJUSTM.CREUSR_0 LIKE 'RFZ%' "
				+ ")"
				
		                ,
                new Object[]{startDate, endDate});
        
		List<GeodeMovement> result = new ArrayList<>();
		GeodeMovement mvt = null;
		
        for(Map<String,Object> row: resultSet ){
        	mvt = new GeodeMovement();
        	mvt.setNumber((String)row.get("ADJNUM_0"));
        	mvt.setItem((String)row.get("ITM_0"));
        	mvt.setQuantity(((BigDecimal)row.get("CUQ_0")).intValue() + ((BigDecimal)row.get("CUQ_1")).intValue());
        	mvt.setCreationDateTime(geodeMovementDateTimeConvert((Timestamp)row.get("DAT_0"), (String)row.get("TIM_0")));
        	mvt.setMovementCode((String)row.get("MVT_0"));
        	mvt.setStore((String)row.get("STO_0"));
        	mvt.setSlot((String)row.get("SLO_0"));
        	mvt.setCreationUserCode((String)row.get("CREUSR_0"));
        	mvt.setCreationUserName((String)row.get("NOMUSR_0"));
        	result.add(mvt);
        }
        
		return result;
	}
	
	private Timestamp geodeMovementDateTimeConvert(Timestamp date, String hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour.substring(0, 2)));
		cal.set(Calendar.MINUTE, Integer.parseInt(hour.substring(2, 4)));
		return new Timestamp(cal.getTimeInMillis());
	}



	@Override
	public boolean checkIfAddressExist(String address) {
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT (GEOATW.SLOT.STO_0 || GEOATW.SLOT.ADD_0) AS ADDRESS FROM GEOATW.SLOT "
				+ "WHERE (GEOATW.SLOT.STO_0 || GEOATW.SLOT.ADD_0) = ?",
                new Object[]{address});

		return !resultSet.isEmpty();
		
	}


	@Override
	public Map<String, Double> getStockOnProductionAndReceptions() {

		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "GEOATW.STOCKOBJ.ITM_0, "
				+ "Sum(GEOATW.STOCKOBJ.CSUQTY_0) AS STOCK "
				+ "FROM "
				+ "GEOATW.STOCKOBJ "
				+ "WHERE "
				+ "GEOATW.STOCKOBJ.STO_0 <> ? "
				+ "AND "
				+ "GEOATW.STOCKOBJ.STO_0 <> ? "
				+ "GROUP BY "
				+ "GEOATW.STOCKOBJ.ITM_0"
				,
                new Object[]{"TMP","J00"});
        
		Map<String, Double> result = new HashMap<>();

        for(Map<String,Object> row: resultSet ){
        	result.put((String)row.get("ITM_0"), ((BigDecimal)row.get("STOCK")).doubleValue());
        }
        
		return result;
	}


	@Override
	public List<GeodeMovement> findShipmentMovementsInPeriod(Date startDate, Date endDate) {
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "GEOATW.ADJUSTM.ADJNUM_0, "
				+ "GEOATW.ADJUSTM.ITM_0, "
				+ "GEOATW.ADJUSTM.CUQ_0, "
				+ "GEOATW.ADJUSTM.CUQ_1, "
				+ "GEOATW.ADJUSTM.DAT_0, "
				+ "GEOATW.ADJUSTM.TIM_0, "
				+ "GEOATW.ADJUSTM.MVT_0, "
				+ "GEOATW.ADJUSTM.STO_0, "
				+ "GEOATW.ADJUSTM.SLO_0, "
				+ "GEOATW.ADJUSTM.CREUSR_0, "
				+ "GEOATW.AUTILIS.NOMUSR_0 "
				+ "FROM "
				+ "GEOATW.ADJUSTM INNER JOIN GEOATW.AUTILIS "
				+ "ON "
				+ "GEOATW.ADJUSTM.CREUSR_0 = GEOATW.AUTILIS.USR_0 "
				+ "WHERE "
				+ "GEOATW.ADJUSTM.DAT_0 >= ? AND "
				+ "GEOATW.ADJUSTM.DAT_0 <= ? AND "
				+ "GEOATW.ADJUSTM.CREUSR_0 LIKE 'RFW%' "
		        ,
                new Object[]{startDate, endDate});
        
		List<GeodeMovement> result = new ArrayList<>();
		GeodeMovement mvt = null;
		
        for(Map<String,Object> row: resultSet ){
        	mvt = new GeodeMovement();
        	mvt.setNumber((String)row.get("ADJNUM_0"));
        	mvt.setItem((String)row.get("ITM_0"));
        	mvt.setQuantity(((BigDecimal)row.get("CUQ_0")).intValue() + ((BigDecimal)row.get("CUQ_1")).intValue());
        	mvt.setCreationDateTime(geodeMovementDateTimeConvert((Timestamp)row.get("DAT_0"), (String)row.get("TIM_0")));
        	mvt.setMovementCode((String)row.get("MVT_0"));
        	mvt.setStore((String)row.get("STO_0"));
        	mvt.setSlot((String)row.get("SLO_0"));
        	mvt.setCreationUserCode((String)row.get("CREUSR_0"));
        	mvt.setCreationUserName((String)row.get("NOMUSR_0"));
        	result.add(mvt);
        }
        
		return result;
	}


	@Override
	public Map<String, Integer> findStockListForStoreType(String type) {
		
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "GEOATW.STOCKOBJ.ITM_0 AS CODE, "
				+ "Sum(GEOATW.STOCKOBJ.CSUQTY_0) AS STOCK "
				+ "FROM "
				+ "GEOATW.STOCKOBJ INNER JOIN GEOATW.STORE "
				+ "ON GEOATW.STOCKOBJ.STO_0 = GEOATW.STORE.STO_0 "
				+ "WHERE "
				+ "GEOATW.STORE.YTYPE_0 = ? "
				+ "GROUP BY "
				+ "GEOATW.STOCKOBJ.ITM_0"
				,
                new Object[]{type});
        
		Map<String, Integer> result = new HashMap<>();

        for(Map<String,Object> row: resultSet ){
        	result.put((String)row.get("CODE"), ((BigDecimal)row.get("STOCK")).intValue());
        }
        
		return result;
	}


	@Override
	public List<GeodeObject> findObjectsByStoreType(String storeType) {
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "GEOATW.STORE.STO_0, "
				+ "GEOATW.STORE.YTYPE_0, "
				+ "GEOATW.STOCKOBJ.ADD_0, "
				+ "GEOATW.STOCKOBJ.SKONUM_0, "
				+ "GEOATW.STOCKOBJ.INPDAT_0,"
				+ "GEOATW.STOCKOBJ.ITM_0, "
				+ "GEOATW.STOCKOBJ.CSUQTY_0 "
				+ "FROM GEOATW.STOCKOBJ INNER JOIN GEOATW.STORE "
				+ "ON "
				+ "GEOATW.STOCKOBJ.STO_0 = GEOATW.STORE.STO_0 "
				+ "WHERE GEOATW.STORE.YTYPE_0 = ?"
		        ,
                new Object[]{storeType});
        
		List<GeodeObject> result = new ArrayList<>();
		GeodeObject obj = null;
		
        for(Map<String,Object> row: resultSet ){
        	obj = new GeodeObject();
        	obj.setStore((String)row.get("STO_0"));
        	obj.setStoreType((String)row.get("YTYPE_0"));
        	obj.setAddress((String)row.get("ADD_0"));
        	obj.setNumber((String)row.get("SKONUM_0"));
        	obj.setInputDate((Timestamp)row.get("INPDAT_0"));
        	obj.setItemCode((String)row.get("ITM_0"));
        	obj.setQuantity(((BigDecimal)row.get("CSUQTY_0")).doubleValue());
        	result.add(obj);
 
        }
        
        return result;
	}


	@Override
	public Map<String, GeodeQuantityObject> findGeneralStockForAllCodes() {

        List<Map<String,Object>> resultSet = jdbc.queryForList(
        		"SELECT "
        		+ "GEOATW.STOCKOBJ.ITM_0 AS ITM, "
        		+ "COUNT(GEOATW.STOCKOBJ.SKONUM_0) AS CNT, "
        		+ "SUM(GEOATW.STOCKOBJ.CSUQTY_0) AS QTY "
        		+ "FROM "
        		+ "GEOATW.STOCKOBJ "
        		+ "GROUP BY GEOATW.STOCKOBJ.ITM_0 "
                ,
                new Object[]{});
        
        Map<String, GeodeQuantityObject> map = new HashMap<>();
        GeodeQuantityObject gq;
        for(Map<String,Object> row: resultSet ){
        	gq = new GeodeQuantityObject();
        	gq.setCode((String)row.get("ITM"));
        	gq.setQuantity(((BigDecimal)row.get("QTY")).intValue());
        	gq.setCount(((BigDecimal)row.get("CNT")).intValue());
        	map.put(gq.getCode(), gq);
        }
        
		return map;
	}


	@Override
	public Map<String, GeodeQuantityObject> findStockForOneStore(String store) {

        List<Map<String,Object>> resultSet = jdbc.queryForList(
        		"SELECT "
        		+ "GEOATW.STOCKOBJ.ITM_0 AS ITM, "
        		+ "COUNT(GEOATW.STOCKOBJ.SKONUM_0) AS CNT, "
        		+ "SUM(GEOATW.STOCKOBJ.CSUQTY_0) AS QTY "
        		+ "FROM "
        		+ "GEOATW.STOCKOBJ "
        		+ "GROUP BY GEOATW.STOCKOBJ.ITM_0, GEOATW.STOCKOBJ.STO_0 "
        		+ "HAVING GEOATW.STOCKOBJ.STO_0 = ? "
                ,
                new Object[]{store});
        
        Map<String, GeodeQuantityObject> map = new HashMap<>();
        GeodeQuantityObject gq;
        for(Map<String,Object> row: resultSet ){
        	gq = new GeodeQuantityObject();
        	gq.setCode((String)row.get("ITM"));
        	gq.setQuantity(((BigDecimal)row.get("QTY")).intValue());
        	gq.setCount(((BigDecimal)row.get("CNT")).intValue());
        	map.put(gq.getCode(), gq);
        }
        
		return map;
	}

}
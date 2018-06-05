package sbs.repository.geode;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import sbs.model.geode.GeodeMovement;

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
                + "ORDER BY "
                + "GEOATW.SLOT.STO_0, GEOATW.SLOT.ADD_0";
		
		return jdbc.queryForList(query, new Object[]{product.toUpperCase()});
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

}
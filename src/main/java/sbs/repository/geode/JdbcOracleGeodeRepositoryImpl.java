package sbs.repository.geode;

import java.util.ArrayList;
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
	public List<GeodeMovement> findMovementsInPeriod(Date startDate, Date endDate) {

		
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
				+ "GEOATW.ADJUSTM.DAT_0 >= ? AND GEOATW.ADJUSTM.DAT_0 <= ? "
		                ,
                new Object[]{startDate, endDate});
        
		List<GeodeMovement> result = new ArrayList<>();
		GeodeMovement mvt = null;
		
        for(Map<String,Object> row: resultSet ){
        	//TODO
        	System.out.println(row);
        	//mvt = new GeodeMovement();
        	//mvt.setSalesNumber((String)row.get("SOHNUM_0"));
        	//order.setOrderDate((Timestamp)row.get("ORDDAT_0"));

        	result.add(mvt);
        }

		return result;
	}

}
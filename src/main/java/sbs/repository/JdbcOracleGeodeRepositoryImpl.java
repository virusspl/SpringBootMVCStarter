package sbs.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
		
		//"SELECT ATW.AUTILIS.NOMUSR_0 FROM ATW.AUTILIS WHERE ATW.AUTILIS.USR_0 = ?"
		return jdbc.queryForList(query, new Object[]{product.toUpperCase()});
	}

}

/*
		String query = "SELECT "
                + "GEOATW.SLOT.STO_0, "
                + "GEOATW.SLOT.ADD_0, "
                + "GEOATW.STOCKOBJ.SKONUM_0, "
                + "GEOATW.STOCKOBJ.ITM_0, "
                + "GEOATW.STOCKOBJ.CSUQTY_0,"
                + "GEOATW.STOCKOBJ.INPDAT_0 "
                + "FROM "
                + "GEOATW.SLOT LEFT JOIN GEOATW.STOCKOBJ "
                + "ON GEOATW.SLOT.ADD_0 = GEOATW.STOCKOBJ.ADD_0 "
                + "AND GEOATW.SLOT.STO_0 = GEOATW.STOCKOBJ.STO_0 "
                + "WHERE UPPER(GEOATW.STOCKOBJ.ITM_0) = ? "
                + "ORDER BY "
                + "GEOATW.SLOT.STO_0, GEOATW.SLOT.ADD_0";
 
*/
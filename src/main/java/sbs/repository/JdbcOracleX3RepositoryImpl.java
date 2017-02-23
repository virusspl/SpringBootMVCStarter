package sbs.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcOracleX3RepositoryImpl implements JdbcOracleX3Repository {

	 @Autowired 
	 @Qualifier("oracleX3JdbcTemplate") 
	 protected JdbcTemplate jdbc;

	@Override
	public List<String> findAllUsers(String company) {
		//"SELECT ATW.AUTILIS.NOMUSR_0 FROM ATW.AUTILIS WHERE ATW.AUTILIS.USR_0 = ?"
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
	public String findItemDescription(String company, String product) {
		List<Map<String,Object>> resultSet = jdbc.queryForList(
				"SELECT " 
						+ company + ".ITMMASTER.ITMDES1_0 "
						+ "FROM " + company + ".ITMMASTER "
						+ "WHERE UPPER(" + company + ".ITMMASTER.ITMREF_0) = ? "
						, new Object[] {product.toUpperCase()});
        String result = null;
        for(Map<String,Object> row: resultSet ){
        	result = (String)row.get("ITMDES1_0");
        }
		return result;
	}

}

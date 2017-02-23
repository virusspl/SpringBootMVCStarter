package sbs.repository;
// http://www.codesenior.com/en/tutorial/Spring-Generic-DAO-and-Generic-Service-Implementation
import java.util.List;
import java.util.Map;

public interface JdbcOracleGeodeRepository  {
    public List<Map<String,Object>> findLocationsOfProduct(String product);
}

package sbs.repository;
// http://www.codesenior.com/en/tutorial/Spring-Generic-DAO-and-Generic-Service-Implementation
import java.util.List;

public interface JdbcOracleX3Repository  {
    public List<String> findAllUsers(String company);
	public String findItemDescription(String company, String product);
}

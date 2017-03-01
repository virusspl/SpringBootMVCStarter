package sbs.service.x3;

import java.util.List;

public interface JdbcOracleX3Service {
	public List<String> findAllUsers(String company);
	public String findItemDescription(String company, String product);
}

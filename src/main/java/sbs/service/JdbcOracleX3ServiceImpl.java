package sbs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sbs.repository.JdbcOracleX3Repository;

@Service
public class JdbcOracleX3ServiceImpl implements JdbcOracleX3Service {
	@Autowired
	JdbcOracleX3Repository jdbcOracleX3Repository;
	
	public List<String> findAllUsers(String company){
		return jdbcOracleX3Repository.findAllUsers(company);
	}

	@Override
	public String findItemDescription(String company, String product) {
		return jdbcOracleX3Repository.findItemDescription(company, product);
	}
}

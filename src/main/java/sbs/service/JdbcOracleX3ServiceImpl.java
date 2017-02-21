package sbs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sbs.repository.JdbcAdrOptimaRepository;

@Service
public class JdbcOracleX3ServiceImpl implements JdbcAdrOptimaService {
	@Autowired
	JdbcAdrOptimaRepository jdbcAdrOptimaRepository;
	
	public List<String> findAllUsers(){
		return jdbcAdrOptimaRepository.findAllUsers();
	}
}

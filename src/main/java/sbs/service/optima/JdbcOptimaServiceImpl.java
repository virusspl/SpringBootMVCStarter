package sbs.service.optima;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sbs.model.hr.HrUserInfo;
import sbs.repository.optima.JdbcOptimaRepository;

@Service
public class JdbcOptimaServiceImpl implements JdbcOptimaService {
	@Autowired
	JdbcOptimaRepository jdbcOptimaRepository;

	@Override
	public HrUserInfo findCurrentlyEmployedById(String userId, int database) {
		return jdbcOptimaRepository.findCurrentlyEmployedById(userId, database);
	}


	@Override
	public HrUserInfo findCurrentlyEmployedByCardNo(String cardNo, int database) {
		return jdbcOptimaRepository.findCurrentlyEmployedByCardNo(cardNo, database);
	}


	@Override
	//@Cacheable(value="allEmployed")
	public List<HrUserInfo> findAllCurrentlyEmployed(int database) {
		return jdbcOptimaRepository.findAllCurrentlyEmployed(database);
	}
}

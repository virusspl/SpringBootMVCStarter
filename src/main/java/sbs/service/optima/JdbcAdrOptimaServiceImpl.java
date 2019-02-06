package sbs.service.optima;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sbs.model.hr.HrUserInfo;
import sbs.repository.optima.JdbcAdrOptimaRepository;

@Service
public class JdbcAdrOptimaServiceImpl implements JdbcAdrOptimaService {
	@Autowired
	JdbcAdrOptimaRepository jdbcAdrOptimaRepository;

	@Override
	public HrUserInfo findCurrentlyEmployedById(String userId) {
		return jdbcAdrOptimaRepository.findCurrentlyEmployedById(userId);
	}


	@Override
	public HrUserInfo findCurrentlyEmployedByCardNo(String cardNo) {
		return jdbcAdrOptimaRepository.findCurrentlyEmployedByCardNo(cardNo);
	}


	@Override
	//@Cacheable(value="allEmployed")
	public List<HrUserInfo> findAllCurrentlyEmployed() {
		return jdbcAdrOptimaRepository.findAllCurrentlyEmployed();
	}
}

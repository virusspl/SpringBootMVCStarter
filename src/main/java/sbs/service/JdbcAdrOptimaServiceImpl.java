package sbs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sbs.model.hr.HrUserInfo;
import sbs.repository.JdbcAdrOptimaRepository;

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
	public List<HrUserInfo> findAllCurrentlyEmployed() {
		return jdbcAdrOptimaRepository.findAllCurrentlyEmployed();
	}
}

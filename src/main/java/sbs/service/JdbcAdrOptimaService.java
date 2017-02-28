package sbs.service;

import java.util.List;

import sbs.model.hr.HrUserInfo;
import sbs.model.hr.RcpInfo;

public interface JdbcAdrOptimaService {
	public List<String> findAllUsers();
    public RcpInfo findRcpInfoByUserId(String userId);
    public RcpInfo findRcpInfoByCardNo(String cardNo);
    public HrUserInfo findCurrentlyEmployedById(String userId);
    public HrUserInfo findCurrentlyEmployedByCardNo(String cardNo);
    public List<HrUserInfo> findAllCurrentlyEmployed();
}

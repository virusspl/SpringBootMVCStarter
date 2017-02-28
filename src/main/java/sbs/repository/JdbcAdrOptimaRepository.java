package sbs.repository;
// http://www.codesenior.com/en/tutorial/Spring-Generic-DAO-and-Generic-Service-Implementation
import java.util.List;

import sbs.model.hr.HrUserInfo;
import sbs.model.hr.RcpInfo;

public interface JdbcAdrOptimaRepository  {
    public List<String> findAllUsers();
    public RcpInfo findRcpInfoByUserId(String userId);
    public RcpInfo findRcpInfoByCardNo(String cardNo);
    public HrUserInfo findCurrentlyEmployedById(String userId);
    public HrUserInfo findCurrentlyEmployedByCardNo(String cardNo);
    public List<HrUserInfo> findAllCurrentlyEmployed();
}

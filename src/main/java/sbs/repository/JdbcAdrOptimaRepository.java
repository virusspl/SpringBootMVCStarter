package sbs.repository;
// http://www.codesenior.com/en/tutorial/Spring-Generic-DAO-and-Generic-Service-Implementation
import java.util.List;

import sbs.model.hr.HrUserInfo;

public interface JdbcAdrOptimaRepository  {

    public HrUserInfo findCurrentlyEmployedById(String userId);
    public HrUserInfo findCurrentlyEmployedByCardNo(String cardNo);
    public List<HrUserInfo> findAllCurrentlyEmployed();
}

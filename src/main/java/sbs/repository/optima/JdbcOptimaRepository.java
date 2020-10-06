package sbs.repository.optima;
// http://www.codesenior.com/en/tutorial/Spring-Generic-DAO-and-Generic-Service-Implementation
import java.util.List;

import sbs.model.hr.HrUserInfo;

public interface JdbcOptimaRepository  {

    public HrUserInfo findCurrentlyEmployedById(String userId, int database);
    public HrUserInfo findCurrentlyEmployedByCardNo(String cardNo, int database);
    public List<HrUserInfo> findAllCurrentlyEmployed(int database);
}

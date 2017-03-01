package sbs.service.optima;

import java.util.List;

import sbs.model.hr.HrUserInfo;

public interface JdbcAdrOptimaService {
    public HrUserInfo findCurrentlyEmployedById(String userId);
    public HrUserInfo findCurrentlyEmployedByCardNo(String cardNo);
    public List<HrUserInfo> findAllCurrentlyEmployed();
}

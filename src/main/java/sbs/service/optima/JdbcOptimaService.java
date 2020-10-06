package sbs.service.optima;

import java.util.List;

import sbs.model.hr.HrUserInfo;

public interface JdbcOptimaService {
	
	public static final int DB_ADR = 1;
	public static final int DB_WPS = 2;
	public static final int DB_ADECCO = 3;
	public static final int DB_UA = 4;
	
	
    public HrUserInfo findCurrentlyEmployedById(String userId, int database);
    public HrUserInfo findCurrentlyEmployedByCardNo(String cardNo, int database);
    public List<HrUserInfo> findAllCurrentlyEmployed(int database);
}

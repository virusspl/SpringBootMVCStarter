package sbs.repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import sbs.helpers.TextHelper;
import sbs.model.hr.HrUserInfo;
import sbs.model.hr.RcpInfo;

@Repository
public class JdbcAdrOptimaRepositoryImpl implements JdbcAdrOptimaRepository {

	@Autowired
	@Qualifier("optimaAdrJdbcTemplate")
	private JdbcTemplate jdbc;
	@Autowired
	private TextHelper textHelper;

	@Override
	public List<String> findAllUsers() {
		List<Map<String, Object>> resultSet = jdbc.queryForList(
				"SELECT [PRI_PriId],[PRI_Nazwisko],[PRI_Imie1] FROM [CDN_ADR].[CDN].[Pracidx]", new Object[] {});
		List<String> result = new ArrayList<>();
		for (Map<String, Object> row : resultSet) {
			result.add(textHelper.capitalizeFull((String) row.get("PRI_Nazwisko") + " " + row.get("PRI_Imie1")));
		}
		return result;
	}
	
	
	/* 
	 * RCP CARD
	 */
	
	@Override
	public RcpInfo findRcpInfoByUserId(String userId){
		RcpInfo rcp = null;
		List<Map<String, Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "[PRI_Kod], "
				+ "[PRI_Nazwisko], "
				+ "[PRI_Imie1], "
				+ "[DZL_Nazwa], "
				+ "[PKR_Numer] "
				+ " FROM ("
				+ "[CDN_ADR].[CDN].[Pracidx] LEFT JOIN [CDN_ADR].[CDN].[PracKartyRcp] ON [PRI_PraId] = [PKR_PrcId]"
				+ ") "
				+ "INNER JOIN [CDN_ADR].[CDN].[Dzialy] ON [PRI_DzlId] = [DZL_DzlId] "
				+ "GROUP BY [PRI_Kod], [PRI_Nazwisko], [PRI_Imie1], [DZL_Nazwa], [PKR_Numer] "
				+ "HAVING [PRI_Kod] = ?"
				, new Object[] {userId});
		
		for (Map<String, Object> row : resultSet) {
			rcp = new RcpInfo();
			rcp.setId((String)row.get("PRI_Kod"));
			rcp.setLastName(textHelper.capitalizeFull((String)row.get("PRI_Nazwisko")));
			rcp.setFirstName(textHelper.capitalizeFull((String)row.get("PRI_Imie1")));
			rcp.setDepartment((String)row.get("DZL_Nazwa"));
			rcp.setRcpNumber((String)row.get("PKR_Numer"));
		}
		return rcp;
	}
	
	@Override
	public RcpInfo findRcpInfoByCardNo(String cardNo) {
		RcpInfo rcp = null;
		List<Map<String, Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "[PRI_Kod], "
				+ "[PRI_Nazwisko], "
				+ "[PRI_Imie1], "
				+ "[DZL_Nazwa], "
				+ "[PKR_Numer] "
				+ " FROM ("
				+ "[CDN_ADR].[CDN].[Pracidx] LEFT JOIN [CDN_ADR].[CDN].[PracKartyRcp] ON [PRI_PraId] = [PKR_PrcId]"
				+ ") "
				+ "INNER JOIN [CDN_ADR].[CDN].[Dzialy] ON [PRI_DzlId] = [DZL_DzlId] "
				+ "GROUP BY [PRI_Kod], [PRI_Nazwisko], [PRI_Imie1], [DZL_Nazwa], [PKR_Numer] "
				+ "HAVING [PKR_Numer] = ?"
				, new Object[] {cardNo});
		
		for (Map<String, Object> row : resultSet) {
			rcp = new RcpInfo();
			rcp.setId((String)row.get("PRI_Kod"));
			rcp.setLastName(textHelper.capitalizeFull((String)row.get("PRI_Nazwisko")));
			rcp.setFirstName(textHelper.capitalizeFull((String)row.get("PRI_Imie1")));
			rcp.setDepartment((String)row.get("DZL_Nazwa"));
			rcp.setRcpNumber((String)row.get("PKR_Numer"));
		}
		return rcp;
	}

	/*
	 * HR EMPLOYED INFO
	 */
	
	@Override
	public HrUserInfo findCurrentlyEmployedById(String userId) {
		HrUserInfo hr = null;
		List<Map<String, Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "[PRI_Kod], "
				+ "[PRI_Nazwisko], "
				+ "[PRI_Imie1], "
				+ "[DZL_Nazwa], "
				+ "[PKR_Numer], "
				+ "[DKM_Nazwa], "
				+ "[PRE_DataOd], "
				+ "[PRE_DataDo] "
				+ "FROM ("
				+ "([CDN_ADR].[CDN].[Pracidx] LEFT JOIN [CDN_ADR].[CDN].[PracKartyRcp] "
				+ "ON [PRI_PraId] = [PKR_PrcId]) "
				+ "INNER JOIN [CDN_ADR].[CDN].[Dzialy] "
				+ "ON [PRI_DzlId] = [DZL_DzlId]) "
				+ "INNER JOIN "
				+ "([CDN_ADR].[CDN].[PracEtaty] INNER JOIN [CDN_ADR].[CDN].[DaneKadMod] "
				+ "ON [PRE_ETADkmIdStanowisko] = [DKM_DkmId]) "
				+ "ON [PRI_Kod] = [PRE_Kod] "
				+ "GROUP BY [PRI_Kod], [PRI_Nazwisko], [PRI_Imie1], [DZL_Nazwa], [PRI_Pesel], "
				+ "[PKR_Numer], [DKM_Nazwa], [PRE_DataOd], [PRE_DataDo] "
				+ "HAVING [PRI_Kod]= ? AND [PRE_DataOd] <= ? AND [PRE_DataDo] >= ?;"
				, new Object[] {userId, new Date(), new Date()});
		
		for (Map<String, Object> row : resultSet) {
			hr = new HrUserInfo();
			hr.setId((String)row.get("PRI_Kod"));
			hr.setLastName(textHelper.capitalizeFull((String)row.get("PRI_Nazwisko")));
			hr.setFirstName(textHelper.capitalizeFull((String)row.get("PRI_Imie1")));
			hr.setDepartment((String)row.get("DZL_Nazwa"));
			hr.setRcpNumber((String)row.get("PKR_Numer"));
			hr.setPosition((String)row.get("DKM_Nazwa"));
			hr.setCurrentJobStart((Timestamp)row.get("PRE_DataOd"));
			hr.setCurrentJobEnd((Timestamp)row.get("PRE_DataDo"));
		}
		return hr;
	}


	@Override
	public HrUserInfo findCurrentlyEmployedByCardNo(String cardNo) {
		HrUserInfo hr = null;
		List<Map<String, Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "[PRI_Kod], "
				+ "[PRI_Nazwisko], "
				+ "[PRI_Imie1], "
				+ "[DZL_Nazwa], "
				+ "[PKR_Numer], "
				+ "[DKM_Nazwa], "
				+ "[PRE_DataOd], "
				+ "[PRE_DataDo] "
				+ "FROM ("
				+ "([CDN_ADR].[CDN].[Pracidx] LEFT JOIN [CDN_ADR].[CDN].[PracKartyRcp] "
				+ "ON [PRI_PraId] = [PKR_PrcId]) "
				+ "INNER JOIN [CDN_ADR].[CDN].[Dzialy] "
				+ "ON [PRI_DzlId] = [DZL_DzlId]) "
				+ "INNER JOIN "
				+ "([CDN_ADR].[CDN].[PracEtaty] INNER JOIN [CDN_ADR].[CDN].[DaneKadMod] "
				+ "ON [PRE_ETADkmIdStanowisko] = [DKM_DkmId]) "
				+ "ON [PRI_Kod] = [PRE_Kod] "
				+ "GROUP BY [PRI_Kod], [PRI_Nazwisko], [PRI_Imie1], [DZL_Nazwa], [PRI_Pesel], "
				+ "[PKR_Numer], [DKM_Nazwa], [PRE_DataOd], [PRE_DataDo] "
				+ "HAVING [PKR_Numer]= ? AND [PRE_DataOd] <= ? AND [PRE_DataDo] >= ?;"
				, new Object[] {cardNo, new Date(), new Date()});
		
		for (Map<String, Object> row : resultSet) {
			hr = new HrUserInfo();
			hr.setId((String)row.get("PRI_Kod"));
			hr.setLastName(textHelper.capitalizeFull((String)row.get("PRI_Nazwisko")));
			hr.setFirstName(textHelper.capitalizeFull((String)row.get("PRI_Imie1")));
			hr.setDepartment((String)row.get("DZL_Nazwa"));
			hr.setRcpNumber((String)row.get("PKR_Numer"));
			hr.setPosition((String)row.get("DKM_Nazwa"));
			hr.setCurrentJobStart((Timestamp)row.get("PRE_DataOd"));
			hr.setCurrentJobEnd((Timestamp)row.get("PRE_DataDo"));
		}
		return hr;
	}
	
	@Override
	public List<HrUserInfo> findAllCurrentlyEmployed() {
		List<Map<String, Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "[PRI_Kod], "
				+ "[PRI_Nazwisko], "
				+ "[PRI_Imie1], "
				+ "[DZL_Nazwa], "
				+ "[PKR_Numer], "
				+ "[DKM_Nazwa], "
				+ "[PRE_DataOd], "
				+ "[PRE_DataDo] "
				+ "FROM ("
				+ "([CDN_ADR].[CDN].[Pracidx] LEFT JOIN [CDN_ADR].[CDN].[PracKartyRcp] "
				+ "ON [PRI_PraId] = [PKR_PrcId]) "
				+ "INNER JOIN [CDN_ADR].[CDN].[Dzialy] "
				+ "ON [PRI_DzlId] = [DZL_DzlId]) "
				+ "INNER JOIN "
				+ "([CDN_ADR].[CDN].[PracEtaty] INNER JOIN [CDN_ADR].[CDN].[DaneKadMod] "
				+ "ON [PRE_ETADkmIdStanowisko] = [DKM_DkmId]) "
				+ "ON [PRI_Kod] = [PRE_Kod] "
				+ "GROUP BY [PRI_Kod], [PRI_Nazwisko], [PRI_Imie1], [DZL_Nazwa], [PRI_Pesel], "
				+ "[PKR_Numer], [DKM_Nazwa], [PRE_DataOd], [PRE_DataDo] "
				+ "HAVING [PRE_DataOd] <= ? AND [PRE_DataDo] >= ?;"
				, new Object[] {new Date(), new Date()});
		
		ArrayList<HrUserInfo> hrList = new ArrayList<>();
		HrUserInfo hr;
		for (Map<String, Object> row : resultSet) {
			hr = new HrUserInfo();
			hr.setId((String)row.get("PRI_Kod"));
			hr.setLastName(textHelper.capitalizeFull((String)row.get("PRI_Nazwisko")));
			hr.setFirstName(textHelper.capitalizeFull((String)row.get("PRI_Imie1")));
			hr.setDepartment((String)row.get("DZL_Nazwa"));
			hr.setRcpNumber((String)row.get("PKR_Numer"));
			hr.setPosition((String)row.get("DKM_Nazwa"));
			hr.setCurrentJobStart((Timestamp)row.get("PRE_DataOd"));
			hr.setCurrentJobEnd((Timestamp)row.get("PRE_DataDo"));
			hrList.add(hr);
		}
		return hrList;
	}
	

}

package sbs.repository.optima;

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

@Repository
public class JdbcAdrOptimaRepositoryImpl implements JdbcAdrOptimaRepository {

	@Autowired
	@Qualifier("optimaAdrJdbcTemplate")
	private JdbcTemplate jdbc;
	@Autowired
	private TextHelper textHelper;

	/*
	 * FIND ALL
	 */
	
	@Override
	public List<HrUserInfo> findAllCurrentlyEmployed() {
		List<Map<String, Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "[PRE_Kod], "
				+ "[PRE_Nazwisko], "
				+ "[PRE_Imie1], "
				+ "[DZL_Nazwa], "
				+ "[PKR_Numer], "
				+ "[DKM_Nazwa], "
				+ "[PRE_DataOd], "
				+ "[PRE_DataDo], "
				+ "[PRE_ZatrudnionyOd], "
				+ "[PRE_ZatrudnionyDo] "
				+ "FROM "
				+ "[CDN_ADR].[CDN].[Dzialy] "
				+ "INNER JOIN (([CDN_ADR].[CDN].[PracEtaty] "
				+ "INNER JOIN [CDN_ADR].[CDN].[DaneKadMod] "
				+ "ON [PRE_ETADkmIdStanowisko] = [DKM_DkmId]) "
				+ "INNER JOIN [CDN_ADR].[CDN].[PracKartyRcp] "
				+ "ON [PRE_PraId] = [PKR_PrcId]) "
				+ "ON [DZL_DzlId] = [PRE_DzlId] "
				+ "GROUP BY "
				+ "[PRE_Kod], [PRE_Nazwisko], [PRE_Imie1], [DZL_Nazwa], [PKR_Numer], "
				+ "[DKM_Nazwa], [PRE_DataOd], [PRE_DataDo], [PRE_ZatrudnionyOd], [PRE_ZatrudnionyDo] "
				+ "HAVING "
				+ "((([PRE_DataOd]) <= ?) AND (([PRE_DataDo]) >= ?)) "
				+ "ORDER BY [PRE_Nazwisko] ASC, [PRE_Imie1] ASC;"
				, new Object[] {new Date(), new Date()});
		
		ArrayList<HrUserInfo> hrList = new ArrayList<>();
		HrUserInfo hr;
		for (Map<String, Object> row : resultSet) {
			hr = new HrUserInfo();
			hr.setId((String)row.get("PRE_Kod"));
			hr.setLastName(textHelper.capitalizeFull((String)row.get("PRE_Nazwisko")));
			hr.setFirstName(textHelper.capitalizeFull((String)row.get("PRE_Imie1")));
			hr.setDepartment((String)row.get("DZL_Nazwa"));
			hr.setRcpNumber((String)row.get("PKR_Numer"));
			hr.setPosition((String)row.get("DKM_Nazwa"));
			hr.setCurrentJobStart((Timestamp)row.get("PRE_DataOd"));
			hr.setCurrentJobEnd((Timestamp)row.get("PRE_DataDo"));
			hr.setEmployDate((Timestamp)row.get("PRE_ZatrudnionyOd"));
			hr.setQuitDate((Timestamp)row.get("PRE_ZatrudnionyDo"));
			hrList.add(hr);
		}
		return hrList;
	}

	/*
	 * FIND BY EMPLOYEE ID
	 */
	
	@Override
	public HrUserInfo findCurrentlyEmployedById(String userId) {
		List<Map<String, Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "[PRE_Kod], "
				+ "[PRE_Nazwisko], "
				+ "[PRE_Imie1], "
				+ "[DZL_Nazwa], "
				+ "[PKR_Numer], "
				+ "[DKM_Nazwa], "
				+ "[PRE_DataOd], "
				+ "[PRE_DataDo], "
				+ "[PRE_ZatrudnionyOd], "
				+ "[PRE_ZatrudnionyDo] "
				+ "FROM "
				+ "[CDN_ADR].[CDN].[Dzialy] "
				+ "INNER JOIN (([CDN_ADR].[CDN].[PracEtaty] "
				+ "INNER JOIN [CDN_ADR].[CDN].[DaneKadMod] "
				+ "ON [PRE_ETADkmIdStanowisko] = [DKM_DkmId]) "
				+ "INNER JOIN [CDN_ADR].[CDN].[PracKartyRcp] "
				+ "ON [PRE_PraId] = [PKR_PrcId]) "
				+ "ON [DZL_DzlId] = [PRE_DzlId] "
				+ "GROUP BY "
				+ "[PRE_Kod], [PRE_Nazwisko], [PRE_Imie1], [DZL_Nazwa], [PKR_Numer], "
				+ "[DKM_Nazwa], [PRE_DataOd], [PRE_DataDo], [PRE_ZatrudnionyOd], [PRE_ZatrudnionyDo] "
				+ "HAVING "
				+ "(([PRE_DataOd] <= ?) AND ([PRE_DataDo] >= ?) AND ([PRE_Kod] = ?));"
				+ ";"
				, new Object[] {new Date(), new Date(), userId});
		
		HrUserInfo hr = null;
		for (Map<String, Object> row : resultSet) {
			hr = new HrUserInfo();
			hr.setId((String)row.get("PRE_Kod"));
			hr.setLastName(textHelper.capitalizeFull((String)row.get("PRE_Nazwisko")));
			hr.setFirstName(textHelper.capitalizeFull((String)row.get("PRE_Imie1")));
			hr.setDepartment((String)row.get("DZL_Nazwa"));
			hr.setRcpNumber((String)row.get("PKR_Numer"));
			hr.setPosition((String)row.get("DKM_Nazwa"));
			hr.setCurrentJobStart((Timestamp)row.get("PRE_DataOd"));
			hr.setCurrentJobEnd((Timestamp)row.get("PRE_DataDo"));
			hr.setEmployDate((Timestamp)row.get("PRE_ZatrudnionyOd"));
			hr.setQuitDate((Timestamp)row.get("PRE_ZatrudnionyDo"));
		}
		return hr;
	}

	/*
	 * FIND BY CARD NO
	 */
	
	@Override
	public HrUserInfo findCurrentlyEmployedByCardNo(String cardNo) {
		List<Map<String, Object>> resultSet = jdbc.queryForList(
				"SELECT "
				+ "[PRE_Kod], "
				+ "[PRE_Nazwisko], "
				+ "[PRE_Imie1], "
				+ "[DZL_Nazwa], "
				+ "[PKR_Numer], "
				+ "[DKM_Nazwa], "
				+ "[PRE_DataOd], "
				+ "[PRE_DataDo], "
				+ "[PRE_ZatrudnionyOd], "
				+ "[PRE_ZatrudnionyDo] "
				+ "FROM "
				+ "[CDN_ADR].[CDN].[Dzialy] "
				+ "INNER JOIN (([CDN_ADR].[CDN].[PracEtaty] "
				+ "INNER JOIN [CDN_ADR].[CDN].[DaneKadMod] "
				+ "ON [PRE_ETADkmIdStanowisko] = [DKM_DkmId]) "
				+ "INNER JOIN [CDN_ADR].[CDN].[PracKartyRcp] "
				+ "ON [PRE_PraId] = [PKR_PrcId]) "
				+ "ON [DZL_DzlId] = [PRE_DzlId] "
				+ "GROUP BY "
				+ "[PRE_Kod], [PRE_Nazwisko], [PRE_Imie1], [DZL_Nazwa], [PKR_Numer], "
				+ "[DKM_Nazwa], [PRE_DataOd], [PRE_DataDo], [PRE_ZatrudnionyOd], [PRE_ZatrudnionyDo] "
				+ "HAVING "
				+ "(([PRE_DataOd] <= ?) AND ([PRE_DataDo] >= ?) AND ([PKR_Numer] = ?));"
				+ ";"
				, new Object[] {new Date(), new Date(), cardNo});
		
		HrUserInfo hr = null;
		for (Map<String, Object> row : resultSet) {
			hr = new HrUserInfo();
			hr.setId((String)row.get("PRE_Kod"));
			hr.setLastName(textHelper.capitalizeFull((String)row.get("PRE_Nazwisko")));
			hr.setFirstName(textHelper.capitalizeFull((String)row.get("PRE_Imie1")));
			hr.setDepartment((String)row.get("DZL_Nazwa"));
			hr.setRcpNumber((String)row.get("PKR_Numer"));
			hr.setPosition((String)row.get("DKM_Nazwa"));
			hr.setCurrentJobStart((Timestamp)row.get("PRE_DataOd"));
			hr.setCurrentJobEnd((Timestamp)row.get("PRE_DataDo"));
			hr.setEmployDate((Timestamp)row.get("PRE_ZatrudnionyOd"));
			hr.setQuitDate((Timestamp)row.get("PRE_ZatrudnionyDo"));
		}
		return hr;
	}
	
}

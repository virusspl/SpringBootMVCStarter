package sbs.repository.optima;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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
		String query = "SELECT "
				+ "PRI_PraId, "
				+ "PRI_Kod, "
				+ "PRI_Nazwisko, " 
				+ "PRI_Imie1, " 
				+ "F.DKM_Nazwa AS [DkmNazwaStanowiska], " 
				+ "PRE_ETAStawka, "
				+ "PRE_ZatrudnionyDo, " 
				+ "D.DZL_Kod AS [DZL_Kod], " 
				+ "PRE_HDKEmail, " 
				+ "E.CNT_Kod AS [CNT_Kod], "
				+ "PRI_PriId, "
				+ "PRA_PraId, "
				+ "PRE_PreId, "
				+ "PRI_Nadrzedny, "
				+ "PRI_ParentId, "
				+ "PRI_Archiwalny, "
				+ "PRE_ETAWymiar, "
				+ "PRE_ZatrudnionyOd, "
				+ "PRE_ETAEtatL, "
				+ "PRE_ETAEtatM, "
				+ "PRE_DzlId, "
				+ "PRE_Oddelegowany, "
				+ "PRE_Pesel, "
				+ "PRE_Tymczasowy, "
				+ "PRE_DkmIdPracodawca, "
				+ "PRE_CntId, "
				+ "PRI_Typ, "
				+ "PRE_PRMPracaRodzic, " 
				+ "PRE_OddelegowanyKraj, " 
				+ "PKR_Numer, " 
				+ "PRE_KategoriaOpis AS [Usr_Column1], " 
				+ "wartoscATR AS [Usr_Column2], "
				+ "wartoscATRPoz AS [Usr_Column3], " 
				+ "wartoscATRKat AS [Usr_Column4], "
				+ "PRE_ETARodzajUmowy AS [Usr_Column5], " 
				+ "PRE_WaznoscBadanOkres AS [Usr_Column6], "
				+ "PRE_HDKUwagi AS [Usr_Column7], "
				+ "PRE_Paszport AS [Usr_Column8] "
				+ "FROM CDN_ADR_S_A_2017.CDN.Pracidx A " 
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.CDN.PracKod B ON B.PRA_PraId = A.PRI_PraId "  
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.CDN.PracEtaty C ON C.PRE_PraId = B.PRA_PraId "
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.CDN.Dzialy D ON C.PRE_DzlId = D.DZL_DzlId "
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.CDN.Centra E ON C.PRE_CntId = E.CNT_CntId "
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.cdn.danekadmod F on C.PRE_ETADkmIdStanowisko = F.DKM_DkmId "
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.CDN.Zaklady Z ON Z.Zak_ZakID = C.PRE_ZakID "
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.CDN.Zeto_Atrybuty AS UsrA ON pri_praid = usra.id "
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.CDN.Zeto_Atrybuty_Poziom AS UsrB ON pri_praid = usrb.id "
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.CDN.Zeto_Atrybuty_Kategoria AS UsrC ON pri_praid = usrc.id "
				+ "LEFT OUTER JOIN (SELECT "
				+ "					PKR_Numer, PKR_PrcId "
				+ "					FROM CDN_ADR_S_A_2017.CDN.PracKartyRcp "
				+ "					WHERE PKR_OkresDo >= ? "
				+ ") AS Krt ON A.PRI_PraId = Krt.PKR_PrcId "				
				+ "WHERE "
				+ "((( "
				+ "	1 = ( " 
				+ "	select top 1 1 " 
				+ "	from CDN_ADR_S_A_2017.CDN.PracEtaty PracEtaty "
				+ "	where "
				+ " 	PRI_PraId = PRE_PraId "
				+ "		and (PRE_DzlId = 3 or PRE_AdresDzialu like '1%') "
				+ "		and PRE_DataOd < ? "
				+ "		and PRE_DataDo >= ? "
				+ "	)  "
				+ "	and PRI_Typ = 1  AND PRI_Archiwalny < 1  "
				+ "	and ? >= PRE_DataOd "
				+ "	and ? <= PRE_DataDo "
				+ "))) "
				+ "ORDER BY PRI_Nazwisko, PRI_Imie1;";
		
		Date today, currentMonthStart, nextMonthStart;
		Calendar cal = Calendar.getInstance();
		today = cal.getTime();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		currentMonthStart = cal.getTime();
		cal.add(Calendar.MONTH, 1);
		nextMonthStart = cal.getTime();
		
		List<Map<String, Object>> resultSet = jdbc.queryForList(
				query
				, new Object[] {today, nextMonthStart, currentMonthStart, today, today });
		
		ArrayList<HrUserInfo> hrList = new ArrayList<>();
		HrUserInfo hr;
		for (Map<String, Object> row : resultSet) {
			hr = new HrUserInfo();
			hr.setId((String)row.get("PRI_Kod"));
			hr.setLastName(textHelper.capitalizeFull((String)row.get("PRI_Nazwisko")));
			hr.setFirstName(textHelper.capitalizeFull((String)row.get("PRI_Imie1")));
			hr.setDepartment((String)row.get("DZL_Kod"));
			hr.setRcpNumber((String)row.get("PKR_Numer"));
			//hr.setRcpNumber(""+row.get("PRI_PraId"));
			hr.setPosition((String)row.get("DkmNazwaStanowiska"));
			hr.setCurrentJobStart((Timestamp)row.get("PRE_ZatrudnionyOd"));
			hr.setCurrentJobEnd((Timestamp)row.get("PRE_ZatrudnionyDo"));
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
		String query = "SELECT "
				+ "PRI_PraId, "
				+ "PRI_Kod, "
				+ "PRI_Nazwisko, " 
				+ "PRI_Imie1, " 
				+ "F.DKM_Nazwa AS [DkmNazwaStanowiska], " 
				+ "PRE_ETAStawka, "
				+ "PRE_ZatrudnionyDo, " 
				+ "D.DZL_Kod AS [DZL_Kod], " 
				+ "PRE_HDKEmail, " 
				+ "E.CNT_Kod AS [CNT_Kod], "
				+ "PRI_PriId, "
				+ "PRA_PraId, "
				+ "PRE_PreId, "
				+ "PRI_Nadrzedny, "
				+ "PRI_ParentId, "
				+ "PRI_Archiwalny, "
				+ "PRE_ETAWymiar, "
				+ "PRE_ZatrudnionyOd, "
				+ "PRE_ETAEtatL, "
				+ "PRE_ETAEtatM, "
				+ "PRE_DzlId, "
				+ "PRE_Oddelegowany, "
				+ "PRE_Pesel, "
				+ "PRE_Tymczasowy, "
				+ "PRE_DkmIdPracodawca, "
				+ "PRE_CntId, "
				+ "PRI_Typ, "
				+ "PRE_PRMPracaRodzic, " 
				+ "PRE_OddelegowanyKraj, " 
				+ "PKR_Numer, " 
				+ "PRE_KategoriaOpis AS [Usr_Column1], " 
				+ "wartoscATR AS [Usr_Column2], "
				+ "wartoscATRPoz AS [Usr_Column3], " 
				+ "wartoscATRKat AS [Usr_Column4], "
				+ "PRE_ETARodzajUmowy AS [Usr_Column5], " 
				+ "PRE_WaznoscBadanOkres AS [Usr_Column6], "
				+ "PRE_HDKUwagi AS [Usr_Column7], "
				+ "PRE_Paszport AS [Usr_Column8] "
				+ "FROM CDN_ADR_S_A_2017.CDN.Pracidx A " 
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.CDN.PracKod B ON B.PRA_PraId = A.PRI_PraId "  
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.CDN.PracEtaty C ON C.PRE_PraId = B.PRA_PraId "
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.CDN.Dzialy D ON C.PRE_DzlId = D.DZL_DzlId "
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.CDN.Centra E ON C.PRE_CntId = E.CNT_CntId "
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.cdn.danekadmod F on C.PRE_ETADkmIdStanowisko = F.DKM_DkmId "
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.CDN.Zaklady Z ON Z.Zak_ZakID = C.PRE_ZakID "
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.CDN.Zeto_Atrybuty AS UsrA ON pri_praid = usra.id "
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.CDN.Zeto_Atrybuty_Poziom AS UsrB ON pri_praid = usrb.id "
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.CDN.Zeto_Atrybuty_Kategoria AS UsrC ON pri_praid = usrc.id "
				+ "LEFT OUTER JOIN (SELECT "
				+ "					PKR_Numer, PKR_PrcId "
				+ "					FROM CDN_ADR_S_A_2017.CDN.PracKartyRcp "
				+ "					WHERE PKR_OkresDo >= ? "
				+ ") AS Krt ON A.PRI_PraId = Krt.PKR_PrcId "				
				+ "WHERE "
				+ "((( "
				+ "	1 = ( " 
				+ "	select top 1 1 " 
				+ "	from CDN_ADR_S_A_2017.CDN.PracEtaty PracEtaty "
				+ "	where "
				+ " 	PRI_PraId = PRE_PraId "
				+ "		and (PRE_DzlId = 3 or PRE_AdresDzialu like '1%') "
				+ "		and PRE_DataOd < ? "
				+ "		and PRE_DataDo >= ? "
				+ "	)  "
				+ "	and PRI_Typ = 1  AND PRI_Archiwalny < 1  "
				+ "	and ? >= PRE_DataOd "
				+ "	and ? <= PRE_DataDo "
				+ " and PRI_Kod = ? "
				+ "))) "
				+ "ORDER BY PRI_Nazwisko, PRI_Imie1;";
		
		Date today, currentMonthStart, nextMonthStart;
		Calendar cal = Calendar.getInstance();
		today = cal.getTime();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		currentMonthStart = cal.getTime();
		cal.add(Calendar.MONTH, 1);
		nextMonthStart = cal.getTime();
		
		List<Map<String, Object>> resultSet = jdbc.queryForList(
				query
				, new Object[] {today, nextMonthStart, currentMonthStart, today, today, userId });
		
		HrUserInfo hr = null;
		for (Map<String, Object> row : resultSet) {
			hr = new HrUserInfo();
			hr.setId((String)row.get("PRI_Kod"));
			hr.setLastName(textHelper.capitalizeFull((String)row.get("PRI_Nazwisko")));
			hr.setFirstName(textHelper.capitalizeFull((String)row.get("PRI_Imie1")));
			hr.setDepartment((String)row.get("DZL_Kod"));
			hr.setRcpNumber((String)row.get("PKR_Numer"));
			//hr.setRcpNumber(""+row.get("PRI_PraId"));
			hr.setPosition((String)row.get("DkmNazwaStanowiska"));
			hr.setCurrentJobStart((Timestamp)row.get("PRE_ZatrudnionyOd"));
			hr.setCurrentJobEnd((Timestamp)row.get("PRE_ZatrudnionyDo"));
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
		String query = "SELECT "
				+ "PRI_PraId, "
				+ "PRI_Kod, "
				+ "PRI_Nazwisko, " 
				+ "PRI_Imie1, " 
				+ "F.DKM_Nazwa AS [DkmNazwaStanowiska], " 
				+ "PRE_ETAStawka, "
				+ "PRE_ZatrudnionyDo, " 
				+ "D.DZL_Kod AS [DZL_Kod], " 
				+ "PRE_HDKEmail, " 
				+ "E.CNT_Kod AS [CNT_Kod], "
				+ "PRI_PriId, "
				+ "PRA_PraId, "
				+ "PRE_PreId, "
				+ "PRI_Nadrzedny, "
				+ "PRI_ParentId, "
				+ "PRI_Archiwalny, "
				+ "PRE_ETAWymiar, "
				+ "PRE_ZatrudnionyOd, "
				+ "PRE_ETAEtatL, "
				+ "PRE_ETAEtatM, "
				+ "PRE_DzlId, "
				+ "PRE_Oddelegowany, "
				+ "PRE_Pesel, "
				+ "PRE_Tymczasowy, "
				+ "PRE_DkmIdPracodawca, "
				+ "PRE_CntId, "
				+ "PRI_Typ, "
				+ "PRE_PRMPracaRodzic, " 
				+ "PRE_OddelegowanyKraj, " 
				+ "PKR_Numer, " 
				+ "PRE_KategoriaOpis AS [Usr_Column1], " 
				+ "wartoscATR AS [Usr_Column2], "
				+ "wartoscATRPoz AS [Usr_Column3], " 
				+ "wartoscATRKat AS [Usr_Column4], "
				+ "PRE_ETARodzajUmowy AS [Usr_Column5], " 
				+ "PRE_WaznoscBadanOkres AS [Usr_Column6], "
				+ "PRE_HDKUwagi AS [Usr_Column7], "
				+ "PRE_Paszport AS [Usr_Column8] "
				+ "FROM CDN_ADR_S_A_2017.CDN.Pracidx A " 
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.CDN.PracKod B ON B.PRA_PraId = A.PRI_PraId "  
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.CDN.PracEtaty C ON C.PRE_PraId = B.PRA_PraId "
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.CDN.Dzialy D ON C.PRE_DzlId = D.DZL_DzlId "
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.CDN.Centra E ON C.PRE_CntId = E.CNT_CntId "
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.cdn.danekadmod F on C.PRE_ETADkmIdStanowisko = F.DKM_DkmId "
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.CDN.Zaklady Z ON Z.Zak_ZakID = C.PRE_ZakID "
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.CDN.Zeto_Atrybuty AS UsrA ON pri_praid = usra.id "
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.CDN.Zeto_Atrybuty_Poziom AS UsrB ON pri_praid = usrb.id "
				+ "LEFT OUTER JOIN CDN_ADR_S_A_2017.CDN.Zeto_Atrybuty_Kategoria AS UsrC ON pri_praid = usrc.id "
				+ "LEFT OUTER JOIN (SELECT "
				+ "					PKR_Numer, PKR_PrcId "
				+ "					FROM CDN_ADR_S_A_2017.CDN.PracKartyRcp "
				+ "					WHERE PKR_OkresDo >= ? "
				+ ") AS Krt ON A.PRI_PraId = Krt.PKR_PrcId "				
				+ "WHERE "
				+ "((( "
				+ "	1 = ( " 
				+ "	select top 1 1 " 
				+ "	from CDN_ADR_S_A_2017.CDN.PracEtaty PracEtaty "
				+ "	where "
				+ " 	PRI_PraId = PRE_PraId "
				+ "		and (PRE_DzlId = 3 or PRE_AdresDzialu like '1%') "
				+ "		and PRE_DataOd < ? "
				+ "		and PRE_DataDo >= ? "
				+ "	)  "
				+ "	and PRI_Typ = 1  AND PRI_Archiwalny < 1  "
				+ "	and ? >= PRE_DataOd "
				+ "	and ? <= PRE_DataDo "
				+ " and PKR_Numer = ? "
				+ "))) "
				+ "ORDER BY PRI_Nazwisko, PRI_Imie1;";
		
		Date today, currentMonthStart, nextMonthStart;
		Calendar cal = Calendar.getInstance();
		today = cal.getTime();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		currentMonthStart = cal.getTime();
		cal.add(Calendar.MONTH, 1);
		nextMonthStart = cal.getTime();
		
		List<Map<String, Object>> resultSet = jdbc.queryForList(
				query
				, new Object[] {today, nextMonthStart, currentMonthStart, today, today, cardNo });
		
		HrUserInfo hr = null;
		for (Map<String, Object> row : resultSet) {
			hr = new HrUserInfo();
			hr.setId((String)row.get("PRI_Kod"));
			hr.setLastName(textHelper.capitalizeFull((String)row.get("PRI_Nazwisko")));
			hr.setFirstName(textHelper.capitalizeFull((String)row.get("PRI_Imie1")));
			hr.setDepartment((String)row.get("DZL_Kod"));
			hr.setRcpNumber((String)row.get("PKR_Numer"));
			//hr.setRcpNumber(""+row.get("PRI_PraId"));
			hr.setPosition((String)row.get("DkmNazwaStanowiska"));
			hr.setCurrentJobStart((Timestamp)row.get("PRE_ZatrudnionyOd"));
			hr.setCurrentJobEnd((Timestamp)row.get("PRE_ZatrudnionyDo"));
			hr.setEmployDate((Timestamp)row.get("PRE_ZatrudnionyOd"));
			hr.setQuitDate((Timestamp)row.get("PRE_ZatrudnionyDo"));
		}
		return hr;
	}
	
}

package sbs.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import sbs.helpers.TextHelper;

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
	
	/*
SELECT CDN_Pracidx.PRI_Kod, CDN_Pracidx.PRI_Nazwisko, CDN_Pracidx.PRI_Imie1, CDN_Dzialy.DZL_Nazwa, CDN_PracKartyRcp.PKR_Numer
FROM (CDN_Pracidx LEFT JOIN CDN_PracKartyRcp ON CDN_Pracidx.PRI_PriId = CDN_PracKartyRcp.PKR_PrcId) INNER JOIN CDN_Dzialy ON CDN_Pracidx.PRI_DzlId = CDN_Dzialy.DZL_DzlId
GROUP BY CDN_Pracidx.PRI_Kod, CDN_Pracidx.PRI_Nazwisko, CDN_Pracidx.PRI_Imie1, CDN_Dzialy.DZL_Nazwa, CDN_PracKartyRcp.PKR_Numer
HAVING (((CDN_Pracidx.PRI_Kod)="1116") AND ((CDN_PracKartyRcp.PKR_Numer)="NRCARD"));
	
	 */
	
	/* EMPLOYED USERS
	 
	 SELECT CDN_Pracidx.PRI_Kod, CDN_Pracidx.PRI_Nazwisko, CDN_Pracidx.PRI_Imie1, CDN_Dzialy.DZL_Nazwa, CDN_Pracidx.PRI_Pesel, CDN_PracKartyRcp.PKR_Numer, CDN_DaneKadMod.DKM_Nazwa, CDN_PracEtaty.PRE_DataOd, CDN_PracEtaty.PRE_DataDo
FROM ((CDN_Pracidx LEFT JOIN CDN_PracKartyRcp ON CDN_Pracidx.PRI_PriId = CDN_PracKartyRcp.PKR_PrcId) INNER JOIN CDN_Dzialy ON CDN_Pracidx.PRI_DzlId = CDN_Dzialy.DZL_DzlId) INNER JOIN (CDN_PracEtaty INNER JOIN CDN_DaneKadMod ON CDN_PracEtaty.PRE_ETADkmIdStanowisko = CDN_DaneKadMod.DKM_DkmId) ON CDN_Pracidx.PRI_Kod = CDN_PracEtaty.PRE_Kod
GROUP BY CDN_Pracidx.PRI_Kod, CDN_Pracidx.PRI_Nazwisko, CDN_Pracidx.PRI_Imie1, CDN_Dzialy.DZL_Nazwa, CDN_Pracidx.PRI_Pesel, CDN_PracKartyRcp.PKR_Numer, CDN_DaneKadMod.DKM_Nazwa, CDN_PracEtaty.PRE_DataOd, CDN_PracEtaty.PRE_DataDo
HAVING (((CDN_Pracidx.PRI_Kod)="MARKA") AND ((CDN_PracKartyRcp.PKR_Numer)="RCPCARD") AND ((CDN_PracEtaty.PRE_DataOd)<=Now()) AND ((CDN_PracEtaty.PRE_DataDo)>=Now()));
	 
	 */

}

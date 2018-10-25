package sbs.service.geode;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sbs.model.geode.GeodeMovement;
import sbs.model.geode.GeolookRow;
import sbs.repository.geode.JdbcOracleGeodeRepository;

@Service
public class JdbcOracleGeodeServiceImpl implements JdbcOracleGeodeService {
	
	@Autowired
	JdbcOracleGeodeRepository jdbcOracleGeodeRepository;


	@Override
	public List<GeolookRow> findLocationsOfProduct(String product) {
		
		List<Map<String, Object>> resultSet = jdbcOracleGeodeRepository.findLocationsOfProduct(product);
		ArrayList<GeolookRow> result = new ArrayList<>();
		
		GeolookRow newrow;
		for (Map<String,Object> row : resultSet){
			newrow = new GeolookRow();
			newrow.setProduct((String)row.get("ITM_0"));
			newrow.setStore((String)row.get("STO_0"));
			newrow.setAddress((String)row.get("ADD_0"));
			newrow.setQuantity((BigDecimal)row.get("CSUQTY_0"));
			newrow.setUnit((String)row.get("STU_0"));
			newrow.setInputDate((Timestamp)row.get("INPDAT_0"));
			newrow.setObject((String)row.get("SKONUM_0"));
			result.add(newrow);
		}
		return result;
	}


	@Override
	public List<GeolookRow> findAllLocationsOfProducts() {
		List<Map<String, Object>> resultSet = jdbcOracleGeodeRepository.findAllLocationsOfProducts();
		ArrayList<GeolookRow> result = new ArrayList<>();
		
		GeolookRow newrow;
		for (Map<String,Object> row : resultSet){
			newrow = new GeolookRow();
			newrow.setProduct((String)row.get("ITM_0"));
			newrow.setStore((String)row.get("STO_0"));
			newrow.setAddress((String)row.get("ADD_0"));
			newrow.setQuantity((BigDecimal)row.get("CSUQTY_0"));
			newrow.setUnit((String)row.get("STU_0"));
			newrow.setInputDate((Timestamp)row.get("INPDAT_0"));
			newrow.setObject((String)row.get("SKONUM_0"));
			result.add(newrow);
		}
		return result;
	}


	@Override
	public List<GeodeMovement> findRcpMovementsInPeriod(Date startDate, Date endDate) {
		return jdbcOracleGeodeRepository.findRcpMovementsInPeriod(startDate, endDate); 
	}


	@Override
	public boolean checkIfAddressExist(String address) {
		return jdbcOracleGeodeRepository.checkIfAddressExist(address);
	}
}

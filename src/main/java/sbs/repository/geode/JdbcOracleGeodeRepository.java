package sbs.repository.geode;
import java.util.Date;
// http://www.codesenior.com/en/tutorial/Spring-Generic-DAO-and-Generic-Service-Implementation
import java.util.List;
import java.util.Map;

import sbs.model.geode.GeodeMovement;

public interface JdbcOracleGeodeRepository  {
    public List<Map<String,Object>> findLocationsOfProduct(String product);
	public List<Map<String, Object>> findAllLocationsOfProducts();
	public List<GeodeMovement> findRcpMovementsInPeriod(Date startDate, Date endDate);
	public boolean checkIfAddressExist(String address);
	public Map<String, Double> getStockOnProductionAndReceptions();
	public List<GeodeMovement> findShipmentMovementsInPeriod(Date startDate, Date endDate);
	public Map<String, Integer> findStockListForStoreType(String type);
}

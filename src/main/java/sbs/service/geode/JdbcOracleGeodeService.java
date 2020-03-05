package sbs.service.geode;

import java.util.Date;
import java.util.List;
import java.util.Map;

import sbs.model.geode.GeodeMovement;
import sbs.model.geode.GeodeObject;
import sbs.model.geode.GeolookRow;

public interface JdbcOracleGeodeService {
	
	public final String STORE_TYPE_PRODUCTION = "PRD";
	public final String STORE_TYPE_RECEPTIONS = "RIC";
	public final String STORE_TYPE_SHIPMENTS = "SPE";
	
	public List<GeolookRow> findLocationsOfProduct(String product);
	public List<GeolookRow> findAllLocationsOfProducts();
	public List<GeodeMovement> findRcpMovementsInPeriod(Date startDate, Date endDate);
	public boolean checkIfAddressExist(String address);
	public Map<String, Double> getStockOnProductionAndReceptions();
	public List<GeodeMovement> findShipmentMovementsInPeriod(Date startDate, Date endDate);
	public Map<String, Integer> findStockListForStoreType(String type);
	public List<GeodeObject> findObjectsByStoreType(String storeType);
}

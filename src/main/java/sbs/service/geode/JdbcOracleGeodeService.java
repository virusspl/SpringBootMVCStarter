package sbs.service.geode;

import java.util.Date;
import java.util.List;

import sbs.model.geode.GeodeMovement;
import sbs.model.geode.GeolookRow;

public interface JdbcOracleGeodeService {
	public List<GeolookRow> findLocationsOfProduct(String product);
	public List<GeolookRow> findAllLocationsOfProducts();
	public List<GeodeMovement> findMovementsInPeriod(Date startDate, Date endDate);
}

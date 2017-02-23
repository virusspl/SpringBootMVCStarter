package sbs.service;

import java.util.List;

import sbs.controller.geolook.GeolookRow;

public interface JdbcOracleGeodeService {
	public List<GeolookRow> findLocationsOfProduct(String product);
}

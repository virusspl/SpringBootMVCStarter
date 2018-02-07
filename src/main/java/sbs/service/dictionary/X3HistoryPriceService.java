package sbs.service.dictionary;

import java.util.Map;

import sbs.model.x3.X3HistoryPrice;
import sbs.service.GenericService;

public interface X3HistoryPriceService extends GenericService<X3HistoryPrice, String>{
	
	public Map<String, Double> findAllX3HistoryPrices();
	
}

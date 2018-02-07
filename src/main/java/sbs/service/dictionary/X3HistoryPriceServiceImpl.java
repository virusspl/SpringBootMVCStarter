package sbs.service.dictionary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import sbs.model.x3.X3HistoryPrice;
import sbs.repository.GenericRepository;
import sbs.repository.dictionary.X3HistoryPriceRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class X3HistoryPriceServiceImpl extends GenericServiceAdapter<X3HistoryPrice, String> implements X3HistoryPriceService{
	
	@SuppressWarnings("unused")
	private X3HistoryPriceRepository x3HistoryPriceRepository;
	
    @Autowired
	public X3HistoryPriceServiceImpl(@Qualifier("x3HistoryPriceRepositoryImpl") GenericRepository<X3HistoryPrice, String> genericRepository) {
			super(genericRepository);
			this.x3HistoryPriceRepository = (X3HistoryPriceRepository) genericRepository;
	}


	@Override
	@Transactional
	@Cacheable(value="x3HistoryPriceMap")
	public Map<String, Double> findAllX3HistoryPrices() {
		List<X3HistoryPrice> list = this.findAll();
		Map<String, Double> map = new HashMap<>();
		for(X3HistoryPrice price: list){
			map.put(price.getCode(), price.getPrice());
		}
		return map;
	}
	

}

package sbs.service.buyorders;

import java.util.List;

import sbs.model.buyorders.BuyOrder;
import sbs.service.GenericService;

public interface BuyOrdersService extends GenericService<BuyOrder, Integer>{

	public List<BuyOrder> findAllDesc();

	
}

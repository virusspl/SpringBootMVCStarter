package sbs.repository.buyorders;

import java.util.List;

import sbs.model.buyorders.BuyOrder;
import sbs.repository.GenericRepository;

public interface BuyOrdersRepository extends GenericRepository<BuyOrder,Integer> {

	public List<BuyOrder> findAllDesc();

	
}


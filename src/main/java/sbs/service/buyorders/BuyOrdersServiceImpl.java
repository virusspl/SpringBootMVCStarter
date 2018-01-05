package sbs.service.buyorders;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.buyorders.BuyOrder;
import sbs.repository.GenericRepository;
import sbs.repository.buyorders.BuyOrdersRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class BuyOrdersServiceImpl extends GenericServiceAdapter<BuyOrder, Integer> implements BuyOrdersService{
	
	private BuyOrdersRepository buyOrdersRepository;
	
    @Autowired
	public BuyOrdersServiceImpl(@Qualifier("buyOrdersRepositoryImpl") GenericRepository<BuyOrder, Integer> genericRepository) {
			super(genericRepository);
			this.buyOrdersRepository = (BuyOrdersRepository) genericRepository;
	}

	@Override
	public List<BuyOrder> findAllDesc() {
		return buyOrdersRepository.findAllDesc();
	}	
	

}

package sbs.repository.buyorders;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import sbs.model.buyorders.BuyOrder;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class BuyOrdersRepositoryImpl extends GenericRepositoryAdapter<BuyOrder, Integer>
		implements BuyOrdersRepository {

	
	@SuppressWarnings("unchecked")
	@Override
	public List<BuyOrder> findAllDesc() {
		Criteria crit = currentSession().createCriteria(BuyOrder.class);
		crit.addOrder(Order.desc("id"));
		return crit.list();
	}


}

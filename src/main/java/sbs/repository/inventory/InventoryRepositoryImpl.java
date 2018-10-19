package sbs.repository.inventory;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sbs.model.inventory.Inventory;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class InventoryRepositoryImpl extends GenericRepositoryAdapter<Inventory, Integer>
		implements InventoryRepository {

	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Inventory> findAllActiveInventories() {
		Criteria crit = currentSession().createCriteria(Inventory.class, "inventory");
		//crit.createAlias("project.state", "state");
		crit.add(Restrictions.eq("inventory.active", true));
		return crit.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Inventory> findAllInactiveInventories() {
		Criteria crit = currentSession().createCriteria(Inventory.class, "inventory");
		//crit.createAlias("project.state", "state");
		crit.add(Restrictions.eq("inventory.active", false));
		return crit.list();
	}


}

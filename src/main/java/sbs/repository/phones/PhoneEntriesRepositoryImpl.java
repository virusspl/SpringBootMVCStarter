package sbs.repository.phones;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sbs.model.phones.PhoneEntry;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class PhoneEntriesRepositoryImpl extends GenericRepositoryAdapter<PhoneEntry, Integer>
		implements PhoneEntriesRepository {

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<PhoneEntry> findByNumber(String number, String version) {
		Criteria crit = currentSession().createCriteria(PhoneEntry.class, "entry");
		//crit.createAlias("project.state", "state");
		crit.add(Restrictions.eq("entry.version", version));
		crit.add(Restrictions.eq("entry.number", number));
		return crit.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<PhoneEntry> findAllOrderByCategoryAndNumber(String version) {
		Criteria crit = currentSession().createCriteria(PhoneEntry.class, "entry");
		crit.createAlias("entry.category", "category");
		crit.add(Restrictions.eq("entry.version", version));
		crit.addOrder(Order.asc("category.order"));
		crit.addOrder(Order.asc("category.name"));
		crit.addOrder(Order.asc("entry.number"));
		return crit.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<PhoneEntry> findAllActiveOrderByCategoryAndNumber(String version) {
		Criteria crit = currentSession().createCriteria(PhoneEntry.class, "entry");
		crit.createAlias("entry.category", "category");
		crit.add(Restrictions.eq("entry.version", version));
		crit.add(Restrictions.eq("entry.active", true));
		crit.addOrder(Order.asc("category.order"));
		crit.addOrder(Order.asc("category.name"));
		crit.addOrder(Order.asc("entry.number"));
		return crit.list();
	}

	/*@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ToolsProject> findToolsProjectsByStateOrder(int order) {
		Criteria crit = currentSession().createCriteria(ToolsProject.class, "project");
		crit.createAlias("project.state", "state");
		crit.add(Restrictions.eq("state.order", order));
		return crit.list();
	}
	*/


}

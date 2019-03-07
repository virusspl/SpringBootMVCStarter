package sbs.repository.phones;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sbs.model.phones.PhoneCategory;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class PhoneCategoriesRepositoryImpl extends GenericRepositoryAdapter<PhoneCategory, Integer>
		implements PhoneCategoriesRepository {

	@SuppressWarnings("unchecked")
	@Override
	public List<PhoneCategory> findAllByAscOrder(String version) {
		Criteria crit = currentSession().createCriteria(PhoneCategory.class, "category");
		//crit.createAlias("project.state", "state");
		crit.add(Restrictions.eq("category.version", version));
		crit.addOrder(Order.asc("order"));
		return crit.list();
	}

}

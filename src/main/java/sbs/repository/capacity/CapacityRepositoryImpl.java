package sbs.repository.capacity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import sbs.model.capacity.CapacityItem;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class CapacityRepositoryImpl extends GenericRepositoryAdapter<CapacityItem, Integer>
		implements CapacityRepository {

	@Override
	public void deleteItemsOnDate(Date date) {
		String hql = "delete from CapacityItem c where c.date = :dat";
		Query query = currentSession().createQuery(hql);
		query.setTimestamp("dat", new Timestamp(date.getTime()));
		query.executeUpdate();
	}

	@Override
	public Timestamp getDateZero() {
		String hql = "from CapacityItem c order by c.date asc";
		@SuppressWarnings("unchecked")
		List<CapacityItem> result = (List<CapacityItem>) 
		currentSession()
		.createQuery(hql)
		.list();
		
		if(!result.isEmpty()){
			return result.get(0).getDate();
		}
		else{
			return new Timestamp(new java.util.Date().getTime());
		}
	}

}



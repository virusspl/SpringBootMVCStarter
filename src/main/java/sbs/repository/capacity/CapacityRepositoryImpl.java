package sbs.repository.capacity;

import java.sql.Timestamp;
import java.util.Date;

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
		String hql = "delete from CapacityItem where date = :dat";
		Query query = currentSession().createQuery(hql);
		query.setTimestamp("dat", new Timestamp(date.getTime()));
		query.executeUpdate();
	}

}



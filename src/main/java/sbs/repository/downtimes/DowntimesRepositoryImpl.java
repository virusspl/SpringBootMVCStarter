package sbs.repository.downtimes;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.downtimes.Downtime;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class DowntimesRepositoryImpl extends GenericRepositoryAdapter<Downtime, Integer>
		implements DowntimesRepository {

	@Override
	public List<Downtime> findAllPending() {
		String hql = "from Downtime d where d.opened = :bool "
				+ " order by d.startDate asc ";
		@SuppressWarnings("unchecked")
		List<Downtime> result = (List<Downtime>) 
		currentSession()
		.createQuery(hql)
		.setBoolean("bool", true)
		.list();

		return result;
	}

	@Override
	public List<Downtime> findWithoutResponseForUser(Long userId) {
		String hql = "from Downtime d where d.responseType.order = :ord "
				+ "and d.cause.responsibleUser.id = :uid";
		@SuppressWarnings("unchecked")
		List<Downtime> result = (List<Downtime>) 
		currentSession()
		.createQuery(hql)
		.setInteger("ord", 10)
		.setLong("uid", userId)
		.list();

		return result;
	}
	
}



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
		String hql = "from Downtime d where d.opened = :bool ";
		hql += " order by d.startDate asc ";
		@SuppressWarnings("unchecked")
		List<Downtime> result = (List<Downtime>) 
		currentSession()
		.createQuery(hql)
		.setBoolean("bool", true)
		.list();

		return result;
	}
	
}



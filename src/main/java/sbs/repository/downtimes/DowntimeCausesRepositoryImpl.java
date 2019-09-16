package sbs.repository.downtimes;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.downtimes.Downtime;
import sbs.model.downtimes.DowntimeCause;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class DowntimeCausesRepositoryImpl extends GenericRepositoryAdapter<DowntimeCause, Integer>
		implements DowntimeCausesRepository {

	@Override
	public boolean isIndependent(DowntimeCause cause) {
		String hql = "from Downtime d where d.cause.id = :id";
		@SuppressWarnings("unchecked")
		List<Downtime> result = (List<Downtime>) 
		currentSession()
		.createQuery(hql)
		.setInteger("id", cause.getId())
		.list();
		
		return result.isEmpty();
	}


}

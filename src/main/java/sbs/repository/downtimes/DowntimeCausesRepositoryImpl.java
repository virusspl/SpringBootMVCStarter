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

	@Override
	public List<DowntimeCause> findByType(String typeInternalTitle, boolean activeOnly) {
		String hql = "from DowntimeCause dc where dc.downtimeType.internalTitle = :title ";
		if(activeOnly){
			hql += " and dc.active = true ";
		}
		hql += " order by dc.order asc ";
		@SuppressWarnings("unchecked")
		List<DowntimeCause> result = (List<DowntimeCause>) 
		currentSession()
		.createQuery(hql)
		.setString("title", typeInternalTitle)
		.list();

		return result;
	}


}

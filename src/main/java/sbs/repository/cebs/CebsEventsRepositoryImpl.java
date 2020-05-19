package sbs.repository.cebs;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.cebs.CebsEvent;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class CebsEventsRepositoryImpl extends GenericRepositoryAdapter<CebsEvent, Integer>
		implements CebsEventsRepository {

	@Override
	public CebsEvent findActiveEvent() {
		String hql = "from CebsEvent r where r.active = :pnd";
		@SuppressWarnings("unchecked")
		List<CebsEvent> result = (List<CebsEvent>) 
		currentSession()
		.createQuery(hql)
		.setBoolean("pnd", true)
		.list();
		
		if(!result.isEmpty()){
			return result.get(0);
		}
		else{
			return null;
		}
	}
	
}



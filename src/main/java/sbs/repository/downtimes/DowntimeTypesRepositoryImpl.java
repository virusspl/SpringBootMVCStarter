package sbs.repository.downtimes;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.downtimes.DowntimeType;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class DowntimeTypesRepositoryImpl extends GenericRepositoryAdapter<DowntimeType, Integer>
		implements DowntimeTypesRepository {

	@Override
	public DowntimeType findByOrder(int order) {
		String hql = "from DowntimeType s where s.order = :ord";
		@SuppressWarnings("unchecked")
		List<DowntimeType> result = (List<DowntimeType>) 
		currentSession()
		.createQuery(hql)
		.setInteger("ord", order)
		.list();
		
		if(!result.isEmpty()){
			return result.get(0);
		}
		else{
			return null;
		}
	}

	@Override
	public DowntimeType findByInternalTitle(String typeInternalTitle) {
		String hql = "from DowntimeType s where s.internalTitle = :title";
		@SuppressWarnings("unchecked")
		List<DowntimeType> result = (List<DowntimeType>) 
		currentSession()
		.createQuery(hql)
		.setString("title", typeInternalTitle)
		.list();
		
		if(!result.isEmpty()){
			return result.get(0);
		}
		else{
			return null;
		}
	}


}

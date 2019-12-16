package sbs.repository.downtimes;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.downtimes.DowntimeResponseType;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class DowntimeResponseTypesRepositoryImpl extends GenericRepositoryAdapter<DowntimeResponseType, Integer>
		implements DowntimeResponseTypesRepository {

	@Override
	public DowntimeResponseType findByOrder(int order) {
		String hql = "from DowntimeResponseType s where s.order = :ord";
		@SuppressWarnings("unchecked")
		List<DowntimeResponseType> result = (List<DowntimeResponseType>) 
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
	public DowntimeResponseType findByInternalTitle(String typeInternalTitle) {
		String hql = "from DowntimeResponseType s where s.internalTitle = :title";
		@SuppressWarnings("unchecked")
		List<DowntimeResponseType> result = (List<DowntimeResponseType>) 
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

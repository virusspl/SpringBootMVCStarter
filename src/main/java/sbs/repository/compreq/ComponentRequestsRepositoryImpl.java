package sbs.repository.compreq;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.compreq.ComponentRequest;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class ComponentRequestsRepositoryImpl extends GenericRepositoryAdapter<ComponentRequest, Integer>
		implements ComponentRequestsRepository {

	@Override
	public List<ComponentRequest> findAllPending() {
		String hql = "from ComponentRequest r where r.pending = :pnd";
		@SuppressWarnings("unchecked")
		List<ComponentRequest> result = (List<ComponentRequest>) 
		currentSession()
		.createQuery(hql)
		.setBoolean("pnd", true)
		.list();
		
		return result;
	}


	
}



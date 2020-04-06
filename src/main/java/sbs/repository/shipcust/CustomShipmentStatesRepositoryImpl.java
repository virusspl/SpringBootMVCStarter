package sbs.repository.shipcust;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.shipcust.ShipCustState;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class CustomShipmentStatesRepositoryImpl extends GenericRepositoryAdapter<ShipCustState, Integer>
		implements CustomShipmentStatesRepository {

	@Override
	public ShipCustState findByOrder(int order) {
		String hql = "from ShipCustState s where s.order = :ord";
		@SuppressWarnings("unchecked")
		List<ShipCustState> result = (List<ShipCustState>) 
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
	public ShipCustState findByInternalTitle(String typeInternalTitle) {
		String hql = "from ShipCustState s where s.internalTitle = :title";
		@SuppressWarnings("unchecked")
		List<ShipCustState> result = (List<ShipCustState>) 
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

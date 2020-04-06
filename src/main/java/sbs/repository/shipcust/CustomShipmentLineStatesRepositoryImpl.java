package sbs.repository.shipcust;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.shipcust.ShipCustLineState;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class CustomShipmentLineStatesRepositoryImpl extends GenericRepositoryAdapter<ShipCustLineState, Integer>
		implements CustomShipmentLineStatesRepository {

	@Override
	public ShipCustLineState findByOrder(int order) {
		String hql = "from ShipCustLineState s where s.order = :ord";
		@SuppressWarnings("unchecked")
		List<ShipCustLineState> result = (List<ShipCustLineState>) 
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
	public ShipCustLineState findByInternalTitle(String typeInternalTitle) {
		String hql = "from ShipCustState s where s.internalTitle = :title";
		@SuppressWarnings("unchecked")
		List<ShipCustLineState> result = (List<ShipCustLineState>) 
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

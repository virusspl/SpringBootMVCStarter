package sbs.repository.shipments;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.shipments.ShipmentState;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class ShipmentStatesRepositoryImpl extends GenericRepositoryAdapter<ShipmentState, Integer>
		implements ShipmentStatesRepository {

	@Override
	public ShipmentState findByOrder(int order) {
		

			String hql = "from ShipmentState s where s.order = :ord";
			@SuppressWarnings("unchecked")
			List<ShipmentState> result = (List<ShipmentState>) 
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
	
	

}

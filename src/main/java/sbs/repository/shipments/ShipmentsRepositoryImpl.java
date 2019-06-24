package sbs.repository.shipments;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.shipments.Shipment;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class ShipmentsRepositoryImpl extends GenericRepositoryAdapter<Shipment, Integer>
		implements ShipmentsRepository {

	@Override
	public List<Shipment> findPending(String company) {
		String hql = "from Shipment s where s.state.order < :ord and s.company = :comp";
		@SuppressWarnings("unchecked")
		List<Shipment> result = (List<Shipment>) 
		currentSession()
		.createQuery(hql)
		.setInteger("ord", 40)
		.setString("comp", company)
		.list();
		
		return result;
	}

	@Override
	public List<Shipment> findShipped(String company) {
		String hql = "from Shipment s where s.state.order = :ord and s.company = :comp";
		@SuppressWarnings("unchecked")
		List<Shipment> result = (List<Shipment>) 
		currentSession()
		.createQuery(hql)
		.setInteger("ord", 40)
		.setString("comp", company)
		.list();
		
		return result;
	}
	

}

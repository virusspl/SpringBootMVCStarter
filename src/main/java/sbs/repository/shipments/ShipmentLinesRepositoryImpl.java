package sbs.repository.shipments;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.shipments.ShipmentLine;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class ShipmentLinesRepositoryImpl extends GenericRepositoryAdapter<ShipmentLine, Integer>
		implements ShipmentLinesRepository {

	@Override
	public List<ShipmentLine> findLinesByShipmentId(int shipmentId) {
		String hql = "from ShipmentLine s where s.shipment.id = :shipmentId";
		@SuppressWarnings("unchecked")
		List<ShipmentLine> result = (List<ShipmentLine>) 
		currentSession()
		.createQuery(hql)
		.setInteger("shipmentId", shipmentId)
		.list();
		
		return result;
	}
	
	

}

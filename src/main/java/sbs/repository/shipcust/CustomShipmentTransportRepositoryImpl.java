package sbs.repository.shipcust;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.shipcust.ShipCustTransport;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class CustomShipmentTransportRepositoryImpl extends GenericRepositoryAdapter<ShipCustTransport, Integer>
		implements CustomShipmentTransportRepository {

	@Override
	public List<ShipCustTransport> findAllActive() {
		String hql = "from ShipCustTransport s where s.active = :bool order by s.name asc";
		@SuppressWarnings("unchecked")
		List<ShipCustTransport> result = (List<ShipCustTransport>) 
		currentSession()
		.createQuery(hql)
		.setBoolean("bool", true)
		.list();
		
		return result;
	}

}

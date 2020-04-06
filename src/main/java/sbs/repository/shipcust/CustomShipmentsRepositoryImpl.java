package sbs.repository.shipcust;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.shipcust.CustomShipment;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class CustomShipmentsRepositoryImpl extends GenericRepositoryAdapter<CustomShipment, Integer>
		implements CustomShipmentsRepository {

	@Override
	public List<CustomShipment> findAllClosed() {
		String hql = "from CustomShipment cs where cs.state.order = :ord "
				+ " order by cs.creationDate asc ";
		@SuppressWarnings("unchecked")
		List<CustomShipment> result = (List<CustomShipment>) 
		currentSession()
		.createQuery(hql)
		.setInteger("ord", 30)
		.list();

		return result;
	}

	@Override
	public List<CustomShipment> findAllPending() {
		String hql = "from CustomShipment cs where cs.state.order < :ord "
				+ " order by cs.creationDate asc ";
		@SuppressWarnings("unchecked")
		List<CustomShipment> result = (List<CustomShipment>) 
		currentSession()
		.createQuery(hql)
		.setInteger("ord", 30)
		.list();

		return result;
	}


}

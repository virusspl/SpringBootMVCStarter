package sbs.repository.shipments;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.shipments.ShipmentLine;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class ShipmentLinesRepositoryImpl extends GenericRepositoryAdapter<ShipmentLine, Integer>
		implements ShipmentLinesRepository {
	
	

}

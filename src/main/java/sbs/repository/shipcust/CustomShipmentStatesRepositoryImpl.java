package sbs.repository.shipcust;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.shipcust.ShipCustState;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class CustomShipmentStatesRepositoryImpl extends GenericRepositoryAdapter<ShipCustState, Integer>
		implements CustomShipmentStatesRepository {


}

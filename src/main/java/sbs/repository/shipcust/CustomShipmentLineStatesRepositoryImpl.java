package sbs.repository.shipcust;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.shipcust.ShipCustLineState;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class CustomShipmentLineStatesRepositoryImpl extends GenericRepositoryAdapter<ShipCustLineState, Integer>
		implements CustomShipmentLineStatesRepository {


}

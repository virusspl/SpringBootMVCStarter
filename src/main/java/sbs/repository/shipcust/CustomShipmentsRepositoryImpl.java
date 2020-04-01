package sbs.repository.shipcust;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.shipcust.CustomShipment;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class CustomShipmentsRepositoryImpl extends GenericRepositoryAdapter<CustomShipment, Integer>
		implements CustomShipmentsRepository {


}

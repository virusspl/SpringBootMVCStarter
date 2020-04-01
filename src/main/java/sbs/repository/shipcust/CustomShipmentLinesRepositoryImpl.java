package sbs.repository.shipcust;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.shipcust.CustomShipmentLine;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class CustomShipmentLinesRepositoryImpl extends GenericRepositoryAdapter<CustomShipmentLine, Integer>
		implements CustomShipmentLinesRepository {


}

package sbs.repository.shipcust;

import java.util.List;

import sbs.model.shipcust.CustomShipmentLine;
import sbs.repository.GenericRepository;

public interface CustomShipmentLinesRepository extends GenericRepository<CustomShipmentLine,Integer> {

	List<CustomShipmentLine> findAllPendingSpare(int spareTypeProdOrAcq);


}


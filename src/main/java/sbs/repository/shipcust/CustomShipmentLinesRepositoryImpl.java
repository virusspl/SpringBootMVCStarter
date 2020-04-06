package sbs.repository.shipcust;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.shipcust.CustomShipmentLine;
import sbs.repository.GenericRepositoryAdapter;
import sbs.service.shipcust.CustomShipmentLinesService;

@Repository
@Transactional
public class CustomShipmentLinesRepositoryImpl extends GenericRepositoryAdapter<CustomShipmentLine, Integer>
		implements CustomShipmentLinesRepository {

	@Override
	public List<CustomShipmentLine> findAllPendingSpare(int spareTypeProdOrAcq) {
		String hql = "";
		if(spareTypeProdOrAcq == CustomShipmentLinesService.SPARE_ACQ) {
			hql = "from CustomShipmentLine line where line.state.order = :ord and line.productCategory = :cat";
		}
		else {
			hql = "from CustomShipmentLine line where line.state.order = :ord and line.productCategory != :cat";
		}
		
		@SuppressWarnings("unchecked")
		List<CustomShipmentLine> result = (List<CustomShipmentLine>) 
		currentSession()
		.createQuery(hql)
		.setInteger("ord", 5)
		.setString("cat", "ACV")
		.list();
		
		return result;
	}
	
}

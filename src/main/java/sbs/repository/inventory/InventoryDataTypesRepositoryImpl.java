package sbs.repository.inventory;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.inventory.InventoryDataType;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class InventoryDataTypesRepositoryImpl extends GenericRepositoryAdapter<InventoryDataType, Integer>
		implements InventoryDataTypesRepository {

	@Override	
	public InventoryDataType findByColumnTypeCode(String columnTypeCode) {
		String hql = "from InventoryDataType s where s.columnTypeCode = :code";
		@SuppressWarnings("unchecked")
		List<InventoryDataType> result = (List<InventoryDataType>) currentSession().createQuery(hql).setString("code", columnTypeCode).list();
		if (result == null || result.isEmpty()) {
			return null;
		} 
		return result.get(0);
	}

	
}

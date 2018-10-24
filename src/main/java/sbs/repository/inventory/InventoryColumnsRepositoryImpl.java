package sbs.repository.inventory;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.inventory.InventoryColumn;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class InventoryColumnsRepositoryImpl extends GenericRepositoryAdapter<InventoryColumn, Integer>
		implements InventoryColumnsRepository {

	@Override
	public InventoryColumn findByInventoryAndDataType(int inventoryId, int dataTypeId) {
		String hql = "from InventoryColumn c where c.inventory.id = :invId and c.inventoryDataType.id = :idtId";
		@SuppressWarnings("unchecked")
		List<InventoryColumn> result = (List<InventoryColumn>) 
		currentSession()
		.createQuery(hql)
		.setInteger("invId", inventoryId)
		.setInteger("idtId", dataTypeId)
		.list();
		
		if (result == null || result.isEmpty()) {
			return null;
		}
		
		return result.get(0);
	}

	@Override
	public List<InventoryColumn> findInventoryColumnsSortByOrder(int inventoryId) {
		String hql = "from InventoryColumn c where c.inventory.id = :invId order by c.order";
		@SuppressWarnings("unchecked")
		List<InventoryColumn> result = (List<InventoryColumn>) 
		currentSession()
		.createQuery(hql)
		.setInteger("invId", inventoryId)
		.list();
		
		return result;
	}


}

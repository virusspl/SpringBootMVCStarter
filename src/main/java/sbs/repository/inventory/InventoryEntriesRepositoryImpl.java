package sbs.repository.inventory;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.inventory.InventoryEntry;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class InventoryEntriesRepositoryImpl extends GenericRepositoryAdapter<InventoryEntry, Integer>
		implements InventoryEntriesRepository {

	@Override
	public List<InventoryEntry> findByInventoryIdAndLabelNumber(int inventoryId, String labelNumber) {
			String hql = "from InventoryEntry e where e.inventory.id = :invId and e.label = :lab";
			@SuppressWarnings("unchecked")
			List<InventoryEntry> result = (List<InventoryEntry>) 
			currentSession()
			.createQuery(hql)
			.setInteger("invId", inventoryId)
			.setString("lab", labelNumber)
			.list();

			return result;		
	}

	
}

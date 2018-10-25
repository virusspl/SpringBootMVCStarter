package sbs.repository.inventory;

import java.util.List;

import sbs.model.inventory.InventoryEntry;
import sbs.repository.GenericRepository;

public interface InventoryEntriesRepository extends GenericRepository<InventoryEntry,Integer> {

	List<InventoryEntry> findByInventoryIdAndLabelNumber(int inventoryId, String labelNumber);
	
}


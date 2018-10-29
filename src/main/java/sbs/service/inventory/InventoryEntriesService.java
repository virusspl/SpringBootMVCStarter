package sbs.service.inventory;

import java.util.List;

import sbs.model.inventory.InventoryEntry;
import sbs.service.GenericService;

public interface InventoryEntriesService extends GenericService<InventoryEntry, Integer>{

	List<InventoryEntry> findByInventoryIdAndLabelNumber(int inventoryId, String labelNumber);

	List<InventoryEntry> findByInventoryIdSortByLines(int inventoryId);
	
}

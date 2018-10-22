package sbs.service.inventory;

import sbs.model.inventory.InventoryColumn;
import sbs.service.GenericService;

public interface InventoryColumnsService extends GenericService<InventoryColumn, Integer>{

	InventoryColumn findByInventoryAndDataType(int inventoryId, int dataTypeId);
	
}

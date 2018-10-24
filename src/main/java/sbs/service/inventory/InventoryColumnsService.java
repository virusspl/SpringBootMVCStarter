package sbs.service.inventory;

import java.util.List;

import sbs.model.inventory.InventoryColumn;
import sbs.service.GenericService;

public interface InventoryColumnsService extends GenericService<InventoryColumn, Integer>{

	InventoryColumn findByInventoryAndDataType(int inventoryId, int dataTypeId);

	List<InventoryColumn> findInventoryColumnsSortByOrder(int inventoryId);
	
}

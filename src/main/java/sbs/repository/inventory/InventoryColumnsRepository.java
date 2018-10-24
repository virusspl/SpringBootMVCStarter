package sbs.repository.inventory;

import java.util.List;

import sbs.model.inventory.InventoryColumn;
import sbs.repository.GenericRepository;

public interface InventoryColumnsRepository extends GenericRepository<InventoryColumn,Integer> {

	InventoryColumn findByInventoryAndDataType(int inventoryId, int dataTypeId);

	List<InventoryColumn> findInventoryColumnsSortByOrder(int inventoryId);

	
}


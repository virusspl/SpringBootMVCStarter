package sbs.service.inventory;

import java.util.List;

import sbs.model.inventory.Inventory;
import sbs.service.GenericService;

public interface InventoryService extends GenericService<Inventory, Integer>{

	public List<Inventory> findAllActiveInventories();
	public List<Inventory> findAllInactiveInventories();
	
}

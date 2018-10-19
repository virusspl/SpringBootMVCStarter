package sbs.repository.inventory;

import java.util.List;

import sbs.model.inventory.Inventory;
import sbs.repository.GenericRepository;

public interface InventoryRepository extends GenericRepository<Inventory,Integer> {

	List<Inventory> findAllActiveInventories();
	List<Inventory> findAllInactiveInventories();

	
	
}


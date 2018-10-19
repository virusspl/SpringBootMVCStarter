package sbs.service.inventory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.inventory.Inventory;
import sbs.repository.GenericRepository;
import sbs.repository.inventory.InventoryRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class InventoryServiceImpl extends GenericServiceAdapter<Inventory, Integer> implements InventoryService{
	
	private InventoryRepository inventoryRepository;
	
    @Autowired
	public InventoryServiceImpl(@Qualifier("inventoryRepositoryImpl") GenericRepository<Inventory, Integer> genericRepository) {
			super(genericRepository);
			this.inventoryRepository = (InventoryRepository) genericRepository;
	}

	@Override
	public List<Inventory> findAllActiveInventories() {
		return inventoryRepository.findAllActiveInventories();
	}

	@Override
	public List<Inventory> findAllInactiveInventories() {
		return inventoryRepository.findAllInactiveInventories();
	}

}

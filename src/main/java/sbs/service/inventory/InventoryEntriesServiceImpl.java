package sbs.service.inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.inventory.InventoryEntry;
import sbs.repository.GenericRepository;
import sbs.repository.inventory.InventoryEntriesRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class InventoryEntriesServiceImpl extends GenericServiceAdapter<InventoryEntry, Integer> implements InventoryEntriesService {
	
	@SuppressWarnings("unused")
	private InventoryEntriesRepository inventoryEntriesRepository;
	
    @Autowired
	public InventoryEntriesServiceImpl(@Qualifier("inventoryEntriesRepositoryImpl") GenericRepository<InventoryEntry, Integer> genericRepository) {
			super(genericRepository);
			this.inventoryEntriesRepository = (InventoryEntriesRepository) genericRepository;
	}


}

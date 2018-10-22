package sbs.service.inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.inventory.InventoryColumn;
import sbs.repository.GenericRepository;
import sbs.repository.inventory.InventoryColumnsRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class InventoryColumnsServiceImpl extends GenericServiceAdapter<InventoryColumn, Integer> implements InventoryColumnsService{
	
	private InventoryColumnsRepository inventoryColumnsRepository;
	
    @Autowired
	public InventoryColumnsServiceImpl(@Qualifier("inventoryColumnsRepositoryImpl") GenericRepository<InventoryColumn, Integer> genericRepository) {
			super(genericRepository);
			this.inventoryColumnsRepository = (InventoryColumnsRepository) genericRepository;
	}

	@Override
	public InventoryColumn findByInventoryAndDataType(int inventoryId, int dataTypeId) {
		return inventoryColumnsRepository.findByInventoryAndDataType(inventoryId,dataTypeId);
	}

}

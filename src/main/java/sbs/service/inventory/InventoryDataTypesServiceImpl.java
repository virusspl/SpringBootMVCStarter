package sbs.service.inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.inventory.InventoryDataType;
import sbs.repository.GenericRepository;
import sbs.repository.inventory.InventoryDataTypesRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class InventoryDataTypesServiceImpl extends GenericServiceAdapter<InventoryDataType, Integer> implements InventoryDataTypesService{
	
	@SuppressWarnings("unused")
	private InventoryDataTypesRepository inventoryDataTypesRepository;
	
    @Autowired
	public InventoryDataTypesServiceImpl(@Qualifier("inventoryDataTypesRepositoryImpl") GenericRepository<InventoryDataType, Integer> genericRepository) {
			super(genericRepository);
			this.inventoryDataTypesRepository = (InventoryDataTypesRepository) genericRepository;
	}

}

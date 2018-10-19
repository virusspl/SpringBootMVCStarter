package sbs.repository.inventory;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.inventory.InventoryDataType;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class InventoryDataTypesRepositoryImpl extends GenericRepositoryAdapter<InventoryDataType, Integer>
		implements InventoryDataTypesRepository {


}

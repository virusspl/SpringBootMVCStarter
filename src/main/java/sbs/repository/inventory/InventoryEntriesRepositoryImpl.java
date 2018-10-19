package sbs.repository.inventory;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.inventory.InventoryEntry;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class InventoryEntriesRepositoryImpl extends GenericRepositoryAdapter<InventoryEntry, Integer>
		implements InventoryEntriesRepository {

	
}

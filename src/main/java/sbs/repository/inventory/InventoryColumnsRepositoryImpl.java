package sbs.repository.inventory;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.inventory.InventoryColumn;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class InventoryColumnsRepositoryImpl extends GenericRepositoryAdapter<InventoryColumn, Integer>
		implements InventoryColumnsRepository {


}

package sbs.repository.capacity;

import java.util.Date;

import sbs.model.capacity.CapacityItem;
import sbs.repository.GenericRepository;

public interface CapacityRepository extends GenericRepository<CapacityItem,Integer> {

	void deleteItemsOnDate(Date date);

}


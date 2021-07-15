package sbs.service.capacity;

import java.sql.Timestamp;
import java.util.Date;

import sbs.model.capacity.CapacityItem;
import sbs.service.GenericService;

public interface CapacityService extends GenericService<CapacityItem, Integer>{

	void deleteItemsOnDate(Date date);

	Timestamp getDateZero();
	
}

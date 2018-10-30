package sbs.service.phones;

import java.util.List;

import sbs.model.phones.PhoneCategory;
import sbs.service.GenericService;

public interface PhoneCategoriesService extends GenericService<PhoneCategory, Integer>{

	List<PhoneCategory> findAllByAscOrder(String version);

	//public ToolsProjectState findByOrder(int stateOrderNo);

}

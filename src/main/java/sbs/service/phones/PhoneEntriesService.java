package sbs.service.phones;

import java.util.List;

import sbs.model.phones.PhoneEntry;
import sbs.service.GenericService;

public interface PhoneEntriesService extends GenericService<PhoneEntry, Integer>{

	List<PhoneEntry> findByNumber(int number);

//	public List<ToolsProject> findToolsProjectsByStateOrder(int order);
	
}

package sbs.repository.phones;

import java.util.List;

import sbs.model.phones.PhoneEntry;
import sbs.repository.GenericRepository;

public interface PhoneEntriesRepository extends GenericRepository<PhoneEntry,Integer> {

	List<PhoneEntry> findByNumber(int number);

	List<PhoneEntry> findAllOrderByCategoryAndNumber();

	
}

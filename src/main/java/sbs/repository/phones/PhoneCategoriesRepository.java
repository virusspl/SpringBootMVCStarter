package sbs.repository.phones;

import java.util.List;

import sbs.model.phones.PhoneCategory;
import sbs.repository.GenericRepository;

public interface PhoneCategoriesRepository extends GenericRepository<PhoneCategory,Integer> {

	List<PhoneCategory> findAllByAscOrder(String version);
	
}


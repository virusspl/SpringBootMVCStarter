package sbs.service.phones;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.phones.PhoneCategory;
import sbs.repository.GenericRepository;
import sbs.repository.phones.PhoneCategoriesRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class PhoneCategoriesServiceImpl extends GenericServiceAdapter<PhoneCategory, Integer> implements PhoneCategoriesService{
	
	
	private PhoneCategoriesRepository phoneCategoriesRepository;
	
    @Autowired
	public PhoneCategoriesServiceImpl(@Qualifier("phoneCategoriesRepositoryImpl") GenericRepository<PhoneCategory, Integer> genericRepository) {
			super(genericRepository);
			this.phoneCategoriesRepository = (PhoneCategoriesRepository) genericRepository;
	}

	@Override
	public List<PhoneCategory> findAllByAscOrder(String version) {
		return phoneCategoriesRepository.findAllByAscOrder(version);
	}


}

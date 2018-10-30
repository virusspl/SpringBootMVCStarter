package sbs.service.phones;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.phones.PhoneEntry;
import sbs.repository.GenericRepository;
import sbs.repository.phones.PhoneEntriesRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class PhoneEntriesServiceImpl extends GenericServiceAdapter<PhoneEntry, Integer> implements PhoneEntriesService{
	
	
	private PhoneEntriesRepository phoneEntriesRepository;
	
    @Autowired
	public PhoneEntriesServiceImpl(@Qualifier("phoneEntriesRepositoryImpl") GenericRepository<PhoneEntry, Integer> genericRepository) {
			super(genericRepository);
			this.phoneEntriesRepository = (PhoneEntriesRepository) genericRepository;
	}

	@Override
	public List<PhoneEntry> findByNumber(String number, String version) {
		return phoneEntriesRepository.findByNumber(number, version);
	}

	@Override
	public List<PhoneEntry> findAllOrderByCategoryAndNumber(String version) {
		return phoneEntriesRepository.findAllOrderByCategoryAndNumber(version);
	}



}

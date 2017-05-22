package sbs.service.bhptickets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.bhptickets.BhpTicket;
import sbs.repository.GenericRepository;
import sbs.repository.bhptickets.BhpTicketsRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class BhpTicketsServiceImpl extends GenericServiceAdapter<BhpTicket, Integer> implements BhpTicketsService{
	
	
	@SuppressWarnings("unused")
	private BhpTicketsRepository bhpTicketsRepository;
	
    @Autowired
	public BhpTicketsServiceImpl(@Qualifier("bhpTicketsRepositoryImpl") GenericRepository<BhpTicket, Integer> genericRepository) {
			super(genericRepository);
			this.bhpTicketsRepository = (BhpTicketsRepository) genericRepository;
	}

}

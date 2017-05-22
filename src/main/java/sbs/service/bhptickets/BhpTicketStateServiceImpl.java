package sbs.service.bhptickets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import sbs.model.bhptickets.BhpTicketState;
import sbs.repository.GenericRepository;
import sbs.repository.bhptickets.BhpTicketStateRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class BhpTicketStateServiceImpl extends GenericServiceAdapter<BhpTicketState, Integer> implements BhpTicketStateService{
	
	
	@SuppressWarnings("unused")
	private BhpTicketStateRepository bhpTicketStateRepository;
	
    @Autowired
	public BhpTicketStateServiceImpl(@Qualifier("bhpTicketStateRepositoryImpl") GenericRepository<BhpTicketState, Integer> genericRepository) {
			super(genericRepository);
			this.bhpTicketStateRepository = (BhpTicketStateRepository) genericRepository;
	}

}

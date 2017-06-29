package sbs.service.bhptickets;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.bhptickets.BhpTicket;
import sbs.model.users.User;
import sbs.repository.GenericRepository;
import sbs.repository.bhptickets.BhpTicketsRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class BhpTicketsServiceImpl extends GenericServiceAdapter<BhpTicket, Integer> implements BhpTicketsService{
	
	
	private BhpTicketsRepository bhpTicketsRepository;
	
    @Autowired
	public BhpTicketsServiceImpl(@Qualifier("bhpTicketsRepositoryImpl") GenericRepository<BhpTicket, Integer> genericRepository) {
			super(genericRepository);
			this.bhpTicketsRepository = (BhpTicketsRepository) genericRepository;
	}

	@Override
	public Set<User> findAllPendingTicketsUsers() {
		return bhpTicketsRepository.findAllPendingTicketsUsers();
	}

	@Override
	public List<BhpTicket> findPendingTicketsByUser(User user) { 		
		return bhpTicketsRepository.findAllPendingTicketsByUser(user);
	}

}

package sbs.service.bhptickets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

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

	@Override
	public List<BhpTicket> findAllNotArchivedTickets() {
		return bhpTicketsRepository.findAllNotArchivedTickets();
	}

	@Override
	public List<BhpTicket> findArchivedTickets() {
		return bhpTicketsRepository.findArchivedTickets();
	}

	@Override
	@Transactional
	public List<BhpTicket> findPendingUtrTickets() {
		List<BhpTicket> all = bhpTicketsRepository.findAllPendingTickets();
		List<BhpTicket> result = new ArrayList<>();
		for(BhpTicket ticket: all){
			if(ticket.getAssignedUser().hasRole("ROLE_BHPTICKETSUTRUSER") || ticket.getState().getOrder()==32 || ticket.getState().getOrder()==35){
				result.add(ticket);
			} 
		}
		return result;
	}
	
	

}

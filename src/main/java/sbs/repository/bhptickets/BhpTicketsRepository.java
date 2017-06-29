package sbs.repository.bhptickets;

import java.util.List;
import java.util.Set;

import sbs.model.bhptickets.BhpTicket;
import sbs.model.users.User;
import sbs.repository.GenericRepository;

public interface BhpTicketsRepository extends GenericRepository<BhpTicket,Integer> {

	Set<User> findAllPendingTicketsUsers();
	List<BhpTicket> findAllPendingTicketsByUser(User user);
	
}


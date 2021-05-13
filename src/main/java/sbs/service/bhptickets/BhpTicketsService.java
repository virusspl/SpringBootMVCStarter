package sbs.service.bhptickets;

import java.util.Date;
import java.util.List;
import java.util.Set;

import sbs.model.bhptickets.BhpTicket;
import sbs.model.users.User;
import sbs.service.GenericService;

public interface BhpTicketsService extends GenericService<BhpTicket, Integer>{

	public Set<User> findAllPendingTicketsUsers();
	public List<BhpTicket> findPendingTicketsByUser(User user);
	public List<BhpTicket> findAllNotArchivedTickets();
	public List<BhpTicket> findArchivedTickets();
	public List<BhpTicket> findPendingUtrTickets();
	public List<BhpTicket> findAllInPeriod(Date startDate, Date endDate);
	
}

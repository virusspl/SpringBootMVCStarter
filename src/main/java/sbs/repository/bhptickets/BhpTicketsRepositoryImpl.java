package sbs.repository.bhptickets;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sbs.model.bhptickets.BhpTicket;
import sbs.model.users.User;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class BhpTicketsRepositoryImpl extends GenericRepositoryAdapter<BhpTicket, Integer>
		implements BhpTicketsRepository {

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Set<User> findAllPendingTicketsUsers() {
		Criteria crit = currentSession().createCriteria(BhpTicket.class, "ticket");
		crit.createAlias("ticket.state", "state");
		crit.createAlias("ticket.assignedUser", "user");
		crit.add(Restrictions.lt("state.order", 40));
		List<BhpTicket> tickets = crit.list();
		Set<User> users = new HashSet<User>();
		for(BhpTicket ticket: tickets){
			users.add(ticket.getAssignedUser());
		}
		return users;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<BhpTicket> findAllPendingTicketsByUser(User user) {
		Criteria crit = currentSession().createCriteria(BhpTicket.class, "ticket");
		crit.add(Restrictions.eq("ticket.assignedUser", user));
		crit.createAlias("ticket.state", "state");
		crit.add(Restrictions.lt("state.order", 40));
		return crit.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<BhpTicket> findAllNotArchivedTickets() {
		Criteria crit = currentSession().createCriteria(BhpTicket.class, "ticket");
		crit.createAlias("ticket.state", "state");
		crit.add(Restrictions.lt("state.order", 90));
		return crit.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<BhpTicket> findArchivedTickets() {
		Criteria crit = currentSession().createCriteria(BhpTicket.class, "ticket");
		crit.createAlias("ticket.state", "state");
		crit.add(Restrictions.ge("state.order", 90));
		return crit.list();
	}
	
}

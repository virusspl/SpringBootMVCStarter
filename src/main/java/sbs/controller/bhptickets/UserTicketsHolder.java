package sbs.controller.bhptickets;

import java.util.List;

import sbs.model.bhptickets.BhpTicket;
import sbs.model.users.User;

public class UserTicketsHolder {

	private User user;
	private List<BhpTicket> tickets;
	
	public UserTicketsHolder() {

	}
	
	public UserTicketsHolder(User user, List<BhpTicket> tickets) {
		this.user = user;
		this.tickets = tickets;
	}	
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<BhpTicket> getTickets() {
		return tickets;
	}
	public void setTickets(List<BhpTicket> tickets) {
		this.tickets = tickets;
	}
	
	
	
}
package sbs.model.bhptickets;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "bhp_ticket_states")
public class BhpTicketState {
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "state")
	Set<BhpTicket> tickets;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "bhps_id")
	private int id;
	
	@Column(name = "bhps_name", length = 35, nullable = false)
	private String operatorFirstName;

	
	public BhpTicketState() {
		 tickets = new HashSet<>();
	}
	
	public Set<BhpTicket> getTickets() {
		return tickets;
	}

	public void setTickets(Set<BhpTicket> tickets) {
		this.tickets = tickets;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOperatorFirstName() {
		return operatorFirstName;
	}

	public void setOperatorFirstName(String operatorFirstName) {
		this.operatorFirstName = operatorFirstName;
	}

	@Override
	public String toString() {
		return "BhpTicketState [tickets=" + tickets + ", id=" + id + ", operatorFirstName=" + operatorFirstName + "]";
	}

	
}

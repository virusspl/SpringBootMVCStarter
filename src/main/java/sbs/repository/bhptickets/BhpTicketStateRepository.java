package sbs.repository.bhptickets;

import sbs.model.bhptickets.BhpTicketState;
import sbs.repository.GenericRepository;

public interface BhpTicketStateRepository extends GenericRepository<BhpTicketState,Integer> {

	public BhpTicketState findByOrder(int stateOrderNo);
	
}


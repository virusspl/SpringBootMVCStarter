package sbs.repository.bhptickets;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.bhptickets.BhpTicket;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class BhpTicketsRepositoryImpl extends GenericRepositoryAdapter<BhpTicket, Integer>
		implements BhpTicketsRepository {
	
}

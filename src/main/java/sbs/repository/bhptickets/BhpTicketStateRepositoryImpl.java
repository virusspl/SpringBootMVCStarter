package sbs.repository.bhptickets;

import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import sbs.model.bhptickets.BhpTicketState;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class BhpTicketStateRepositoryImpl extends GenericRepositoryAdapter<BhpTicketState, Integer>
		implements BhpTicketStateRepository {
}

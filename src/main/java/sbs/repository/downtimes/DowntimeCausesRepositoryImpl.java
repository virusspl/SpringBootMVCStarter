package sbs.repository.downtimes;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.downtimes.DowntimeCause;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class DowntimeCausesRepositoryImpl extends GenericRepositoryAdapter<DowntimeCause, Integer>
		implements DowntimeCausesRepository {


}

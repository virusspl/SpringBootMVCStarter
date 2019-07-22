package sbs.repository.downtimes;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.downtimes.Downtime;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class DowntimesRepositoryImpl extends GenericRepositoryAdapter<Downtime, Integer>
		implements DowntimesRepository {


}

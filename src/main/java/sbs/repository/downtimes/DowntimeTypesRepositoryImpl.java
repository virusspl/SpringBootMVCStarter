package sbs.repository.downtimes;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.downtimes.DowntimeType;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class DowntimeTypesRepositoryImpl extends GenericRepositoryAdapter<DowntimeType, Integer>
		implements DowntimeTypesRepository {


}

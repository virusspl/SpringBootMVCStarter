package sbs.repository.downtimes;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.downtimes.DowntimeDetailsFailure;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class DowntimeDetailsFailureRepositoryImpl extends GenericRepositoryAdapter<DowntimeDetailsFailure, Integer>
		implements DowntimeDetailsFailureRepository {


}

package sbs.repository.downtimes;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.downtimes.DowntimeDetailsMaterial;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class DowntimeDetailsMaterialRepositoryImpl extends GenericRepositoryAdapter<DowntimeDetailsMaterial, Integer>
		implements DowntimeDetailsMaterialRepository {


}

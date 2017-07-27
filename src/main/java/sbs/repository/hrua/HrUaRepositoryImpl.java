package sbs.repository.hrua;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.hruafiles.HrUaInfo;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class HrUaRepositoryImpl extends GenericRepositoryAdapter<HrUaInfo, Integer>
		implements HrUaRepository {

}
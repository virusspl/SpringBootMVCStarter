package sbs.repository.hrua;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sbs.model.hruafiles.HrUaInfo;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class HrUaRepositoryImpl extends GenericRepositoryAdapter<HrUaInfo, Integer>
		implements HrUaRepository {

	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<HrUaInfo> findCurrent() {
		Criteria crit = currentSession().createCriteria(HrUaInfo.class, "file");
		crit.add(Restrictions.eq("file.archive", false));
		return crit.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<HrUaInfo> findArchived() {
		Criteria crit = currentSession().createCriteria(HrUaInfo.class, "file");
		crit.add(Restrictions.eq("file.archive", true));
		return crit.list();
	}

}
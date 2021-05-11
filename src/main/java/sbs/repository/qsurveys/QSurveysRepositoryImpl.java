package sbs.repository.qsurveys;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.qsurveys.QSurvey;
import sbs.repository.GenericRepositoryAdapter;

@Repository("qSurveysRepositoryImpl")
@Transactional
public class QSurveysRepositoryImpl extends GenericRepositoryAdapter<QSurvey, Integer> implements QSurveysRepository {

	@Override
	public List<QSurvey> findAllSortByDateDesc() {
		String hql = "from QSurvey s ORDER BY creationDate DESC";
		@SuppressWarnings("unchecked")
		List<QSurvey> result = (List<QSurvey>) currentSession().createQuery(hql).list();
		return result;
	}

	@Override
	public List<QSurvey> findInPeriodSortByDateDesc(Date startDate, Date endDate) {
		String hql = "from QSurvey s WHERE s.creationDate >= :startDate AND  s.creationDate <= :endDate ORDER BY creationDate DESC";
		endDate = new java.util.Date(endDate.getTime() + (1000*60*60*24));
		@SuppressWarnings("unchecked")
		List<QSurvey> result = (List<QSurvey>) currentSession()
				.createQuery(hql)
				.setDate("startDate", startDate)
				.setDate("endDate", endDate).list()
				;
		return result;
	}

}

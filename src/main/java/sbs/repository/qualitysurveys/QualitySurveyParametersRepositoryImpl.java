package sbs.repository.qualitysurveys;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sbs.model.qualitysurveys.QualitySurveyParameter;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class QualitySurveyParametersRepositoryImpl extends GenericRepositoryAdapter<QualitySurveyParameter, Integer>
		implements QualitySurveyParametersRepository {

	@SuppressWarnings("unchecked")
	@Override
	public List<QualitySurveyParameter> findAllActive() {

		Criteria crit = currentSession().createCriteria(QualitySurveyParameter.class);
		crit.add(Restrictions.eq("active", true));
		return crit.list();
	}

}

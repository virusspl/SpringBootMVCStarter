package sbs.repository.qsurveys;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.qsurveys.QSurveyAnswer;
import sbs.repository.GenericRepositoryAdapter;

@Repository("qSurveyAnswersRepositoryImpl")
@Transactional
public class QSurveyAnswersRepositoryImpl extends GenericRepositoryAdapter<QSurveyAnswer, Integer>
		implements QSurveyAnswersRepository {

	@Override
	public List<QSurveyAnswer> findBySurveyId(int surveyId) {
		String hql = "from QSurveyAnswer t WHERE survey.id = :surveyId ORDER BY order ASC";
		@SuppressWarnings("unchecked")
		List<QSurveyAnswer> result = (List<QSurveyAnswer>) currentSession().createQuery(hql).setInteger("surveyId", surveyId).list();
		return result;
	}
	
	
}

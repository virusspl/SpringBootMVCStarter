package sbs.repository.qsurveys;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.qsurveys.QSurveyBomAnswer;
import sbs.repository.GenericRepositoryAdapter;

@Repository("qSurveyBomAnswersRepositoryImpl")
@Transactional
public class QSurveyBomAnswersRepositoryImpl extends GenericRepositoryAdapter<QSurveyBomAnswer, Integer>
		implements QSurveyBomAnswersRepository {

	@Override
	public List<QSurveyBomAnswer> findBySurveyId(int surveyId) {
		String hql = "from QSurveyBomAnswer t WHERE survey.id = :surveyId ORDER BY bomSeq ASC";
		@SuppressWarnings("unchecked")
		List<QSurveyBomAnswer> result = (List<QSurveyBomAnswer>) currentSession().createQuery(hql).setInteger("surveyId", surveyId).list();
		return result;
	}
	
	
}

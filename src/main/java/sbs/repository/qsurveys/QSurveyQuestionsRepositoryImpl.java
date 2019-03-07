package sbs.repository.qsurveys;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.qsurveys.QSurveyQuestion;
import sbs.repository.GenericRepositoryAdapter;

@Repository("qSurveyQuestionsRepositoryImpl")
@Transactional
public class QSurveyQuestionsRepositoryImpl extends GenericRepositoryAdapter<QSurveyQuestion, Integer>
		implements QSurveyQuestionsRepository {

	@Override
	public List<QSurveyQuestion> findListByTemplateIdAsc(int templateId) {
		String hql = "from QSurveyQuestion t WHERE template.id = :templateId ORDER BY order ASC";
		@SuppressWarnings("unchecked")
		List<QSurveyQuestion> result = (List<QSurveyQuestion>) currentSession().createQuery(hql).setInteger("templateId", templateId).list();
		return result;
	}

	
}

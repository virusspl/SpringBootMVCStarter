package sbs.repository.qsurveys;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.qsurveys.QSurveyQuestionType;
import sbs.repository.GenericRepositoryAdapter;

@Repository("qSurveyQuestionTypesRepositoryImpl")
@Transactional
public class QSurveyQuestionTypesRepositoryImpl extends GenericRepositoryAdapter<QSurveyQuestionType, Integer>
		implements QSurveyQuestionTypesRepository {

	@Override
	public QSurveyQuestionType findByCode(String code) {

		String hql = "from QSurveyQuestionType t where code = :typeCode";
		@SuppressWarnings("unchecked")
		List<QSurveyQuestionType> result = (List<QSurveyQuestionType>) currentSession().createQuery(hql).setString("typeCode", code).list();
		if (result == null || result.isEmpty()) {
			return null;
		} 
		return result.get(0);
		
	}

	
	
}

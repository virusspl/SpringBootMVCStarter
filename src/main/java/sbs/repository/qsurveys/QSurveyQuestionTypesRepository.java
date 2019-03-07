package sbs.repository.qsurveys;

import sbs.model.qsurveys.QSurveyQuestionType;
import sbs.repository.GenericRepository;

public interface QSurveyQuestionTypesRepository extends GenericRepository<QSurveyQuestionType,Integer> {

	QSurveyQuestionType findByCode(String code);
	
}


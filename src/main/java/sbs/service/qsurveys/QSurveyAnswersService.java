package sbs.service.qsurveys;

import java.util.List;

import sbs.model.qsurveys.QSurveyAnswer;
import sbs.service.GenericService;

public interface QSurveyAnswersService extends GenericService<QSurveyAnswer, Integer>{

	List<QSurveyAnswer> findBySurveyId(int surveyId);
	
	
}

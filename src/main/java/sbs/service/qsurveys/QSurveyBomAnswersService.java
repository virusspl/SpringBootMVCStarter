package sbs.service.qsurveys;

import java.util.List;

import sbs.model.qsurveys.QSurveyBomAnswer;
import sbs.service.GenericService;

public interface QSurveyBomAnswersService extends GenericService<QSurveyBomAnswer, Integer>{

	List<QSurveyBomAnswer> findBySurveyId(int surveyId);
	
	
}

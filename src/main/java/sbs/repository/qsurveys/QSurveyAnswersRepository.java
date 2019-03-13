package sbs.repository.qsurveys;

import java.util.List;

import sbs.model.qsurveys.QSurveyAnswer;
import sbs.repository.GenericRepository;

public interface QSurveyAnswersRepository extends GenericRepository<QSurveyAnswer,Integer> {

	List<QSurveyAnswer> findBySurveyId(int surveyId);
	
}


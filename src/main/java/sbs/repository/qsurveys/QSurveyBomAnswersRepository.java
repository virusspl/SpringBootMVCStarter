package sbs.repository.qsurveys;

import java.util.List;

import sbs.model.qsurveys.QSurveyBomAnswer;
import sbs.repository.GenericRepository;

public interface QSurveyBomAnswersRepository extends GenericRepository<QSurveyBomAnswer,Integer> {

	List<QSurveyBomAnswer> findBySurveyId(int surveyId);
	
}


package sbs.repository.qsurveys;

import java.util.List;

import sbs.model.qsurveys.QSurveyQuestion;
import sbs.repository.GenericRepository;

public interface QSurveyQuestionsRepository extends GenericRepository<QSurveyQuestion,Integer> {

	List<QSurveyQuestion> findListByTemplateIdAsc(int templateId);
	
}


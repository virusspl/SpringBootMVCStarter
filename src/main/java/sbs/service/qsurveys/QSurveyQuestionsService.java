package sbs.service.qsurveys;

import java.util.List;

import sbs.model.qsurveys.QSurveyQuestion;
import sbs.service.GenericService;

public interface QSurveyQuestionsService extends GenericService<QSurveyQuestion, Integer>{

	List <QSurveyQuestion> findListByTemplateIdAsc(int templateId);
	
	
}

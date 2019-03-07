package sbs.service.qsurveys;

import java.util.List;

import sbs.model.qsurveys.QSurveyTemplate;
import sbs.service.GenericService;

public interface QSurveyTemplatesService extends GenericService<QSurveyTemplate, Integer>{

	public List<QSurveyTemplate> findAllActiveAscByTitle();
	
	
}

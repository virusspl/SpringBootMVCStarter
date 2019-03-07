package sbs.repository.qsurveys;

import java.util.List;

import sbs.model.qsurveys.QSurveyTemplate;
import sbs.repository.GenericRepository;

public interface QSurveyTemplatesRepository extends GenericRepository<QSurveyTemplate,Integer> {

	List<QSurveyTemplate> findAllActiveAscByTitle();
	
}


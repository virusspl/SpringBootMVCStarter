package sbs.service.qualitysurveys;

import java.util.List;
import sbs.model.qualitysurveys.QualitySurvey;
import sbs.service.GenericService;

public interface QualitySurveysService extends GenericService<QualitySurvey, Integer>{
	
	public List<QualitySurvey> findAllDesc();
	
}

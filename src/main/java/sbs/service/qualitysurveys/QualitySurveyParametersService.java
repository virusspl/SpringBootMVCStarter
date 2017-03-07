package sbs.service.qualitysurveys;

import java.util.List;

import sbs.model.qualitysurveys.QualitySurveyParameter;
import sbs.service.GenericService;

public interface QualitySurveyParametersService extends GenericService<QualitySurveyParameter, Integer>{
	public List<QualitySurveyParameter> findAllActive();
}

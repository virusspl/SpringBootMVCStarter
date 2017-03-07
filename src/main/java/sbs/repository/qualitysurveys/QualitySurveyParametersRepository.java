package sbs.repository.qualitysurveys;

import java.util.List;

import sbs.model.qualitysurveys.QualitySurveyParameter;
import sbs.repository.GenericRepository;

public interface QualitySurveyParametersRepository extends GenericRepository<QualitySurveyParameter,Integer> {
	
	List<QualitySurveyParameter> findAllActive();
}


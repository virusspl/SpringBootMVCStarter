package sbs.repository.qualitysurveys;

import java.util.List;

import sbs.model.qualitysurveys.QualitySurvey;
import sbs.repository.GenericRepository;

public interface QualitySurveysRepository extends GenericRepository<QualitySurvey,Integer> {
	
	public List<QualitySurvey> findAllDesc();
	
}


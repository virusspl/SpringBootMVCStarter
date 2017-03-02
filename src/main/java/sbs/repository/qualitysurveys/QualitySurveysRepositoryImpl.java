package sbs.repository.qualitysurveys;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.qualitysurveys.QualitySurvey;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class QualitySurveysRepositoryImpl extends GenericRepositoryAdapter<QualitySurvey,Integer> implements QualitySurveysRepository {
	
}

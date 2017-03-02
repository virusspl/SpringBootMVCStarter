package sbs.repository.qualitysurveys;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.qualitysurveys.QualitySurveyParameter;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class QualitySurveyParametersRepositoryImpl extends GenericRepositoryAdapter<QualitySurveyParameter,Integer> implements QualitySurveyParametersRepository {
	
}

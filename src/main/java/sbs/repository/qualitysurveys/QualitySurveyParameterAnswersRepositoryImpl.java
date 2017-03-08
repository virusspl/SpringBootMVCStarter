package sbs.repository.qualitysurveys;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.qualitysurveys.QualitySurveyParameterAnswer;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class QualitySurveyParameterAnswersRepositoryImpl extends GenericRepositoryAdapter<QualitySurveyParameterAnswer, Integer>
		implements QualitySurveyParameterAnswersRepository {

}

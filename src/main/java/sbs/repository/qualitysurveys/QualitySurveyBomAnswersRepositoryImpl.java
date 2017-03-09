package sbs.repository.qualitysurveys;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.qualitysurveys.QualitySurveyBomAnswer;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class QualitySurveyBomAnswersRepositoryImpl extends GenericRepositoryAdapter<QualitySurveyBomAnswer, Integer>
		implements QualitySurveyBomAnswersRepository {

}

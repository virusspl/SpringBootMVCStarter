package sbs.service.qualitysurveys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.qualitysurveys.QualitySurveyParameterAnswer;
import sbs.repository.GenericRepository;
import sbs.repository.qualitysurveys.QualitySurveyParameterAnswersRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class QualitySurveyParameterAnswersServiceImpl extends GenericServiceAdapter<QualitySurveyParameterAnswer, Integer> implements QualitySurveyParameterAnswersService{
	
	
	@SuppressWarnings("unused")
	private QualitySurveyParameterAnswersRepository qualitySurveyParameterAnswersRepository;
	
    @Autowired
	public QualitySurveyParameterAnswersServiceImpl(@Qualifier("qualitySurveyParameterAnswersRepositoryImpl") GenericRepository<QualitySurveyParameterAnswer, Integer> genericRepository) {
			super(genericRepository);
			this.qualitySurveyParameterAnswersRepository = (QualitySurveyParameterAnswersRepository) genericRepository;
	}

}

package sbs.service.qualitysurveys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.qualitysurveys.QualitySurveyBomAnswer;
import sbs.repository.GenericRepository;
import sbs.repository.qualitysurveys.QualitySurveyBomAnswersRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class QualitySurveyBomAnswersServiceImpl extends GenericServiceAdapter<QualitySurveyBomAnswer, Integer> implements QualitySurveyBomAnswersService{
	
	
	@SuppressWarnings("unused")
	private QualitySurveyBomAnswersRepository qualitySurveyBomAnswersRepository;
	
    @Autowired
	public QualitySurveyBomAnswersServiceImpl(@Qualifier("qualitySurveyBomAnswersRepositoryImpl") GenericRepository<QualitySurveyBomAnswer, Integer> genericRepository) {
			super(genericRepository);
			this.qualitySurveyBomAnswersRepository = (QualitySurveyBomAnswersRepository) genericRepository;
	}

}

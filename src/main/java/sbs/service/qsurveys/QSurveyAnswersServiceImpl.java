package sbs.service.qsurveys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.qsurveys.QSurveyAnswer;
import sbs.repository.GenericRepository;
import sbs.repository.qsurveys.QSurveyAnswersRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class QSurveyAnswersServiceImpl extends GenericServiceAdapter<QSurveyAnswer, Integer> implements QSurveyAnswersService{
	
	private QSurveyAnswersRepository qSurveyAnswersRepository;
	
	@Autowired
	public QSurveyAnswersServiceImpl(@Qualifier("qSurveyAnswersRepositoryImpl") GenericRepository<QSurveyAnswer, Integer> genericRepository) {
			super(genericRepository);
			this.qSurveyAnswersRepository = (QSurveyAnswersRepository) genericRepository;
	}

	@Override
	public List<QSurveyAnswer> findBySurveyId(int surveyId) {
		return qSurveyAnswersRepository.findBySurveyId(surveyId);
	}

}

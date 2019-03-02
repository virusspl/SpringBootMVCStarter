package sbs.service.qsurveys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.qsurveys.QSurveyQuestionType;
import sbs.repository.GenericRepository;
import sbs.repository.qsurveys.QSurveyQuestionTypesRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class QSurveyQuestionTypesServiceImpl extends GenericServiceAdapter<QSurveyQuestionType, Integer> implements QSurveyQuestionTypesService{
	
	@SuppressWarnings("unused")
	private QSurveyQuestionTypesRepository qSurveyQuestionTypesRepository;
	
    @Autowired
	public QSurveyQuestionTypesServiceImpl(@Qualifier("qSurveyQuestionTypesRepositoryImpl") GenericRepository<QSurveyQuestionType, Integer> genericRepository) {
			super(genericRepository);
			this.qSurveyQuestionTypesRepository = (QSurveyQuestionTypesRepository) genericRepository;
	}
	

}

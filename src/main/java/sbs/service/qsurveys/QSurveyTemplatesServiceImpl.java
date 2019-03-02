package sbs.service.qsurveys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.qsurveys.QSurveyTemplate;
import sbs.repository.GenericRepository;
import sbs.repository.qsurveys.QSurveyTemplatesRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class QSurveyTemplatesServiceImpl extends GenericServiceAdapter<QSurveyTemplate, Integer> implements QSurveyTemplatesService{
	
	@SuppressWarnings("unused")
	private QSurveyTemplatesRepository qSurveyTemplatesRepository;
	
    @Autowired
	public QSurveyTemplatesServiceImpl(@Qualifier("qSurveyTemplatesRepositoryImpl") GenericRepository<QSurveyTemplate, Integer> genericRepository) {
			super(genericRepository);
			this.qSurveyTemplatesRepository = (QSurveyTemplatesRepository) genericRepository;
	}
	

}

package sbs.service.qsurveys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.qsurveys.QSurveyTemplate;
import sbs.repository.GenericRepository;
import sbs.repository.qsurveys.QSurveyTemplatesRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class QSurveyTemplatesServiceImpl extends GenericServiceAdapter<QSurveyTemplate, Integer> implements QSurveyTemplatesService{
	
	private QSurveyTemplatesRepository qSurveyTemplatesRepository;

    @Autowired
	public QSurveyTemplatesServiceImpl(@Qualifier("qSurveyTemplatesRepositoryImpl") GenericRepository<QSurveyTemplate, Integer> genericRepository) {
			super(genericRepository);
			this.qSurveyTemplatesRepository = (QSurveyTemplatesRepository) genericRepository;
	}

	@Override
	public List<QSurveyTemplate> findAllActiveAscByTitle() {
		return qSurveyTemplatesRepository.findAllActiveAscByTitle();
	}


}

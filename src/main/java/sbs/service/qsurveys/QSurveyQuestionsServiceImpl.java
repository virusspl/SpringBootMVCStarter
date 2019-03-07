package sbs.service.qsurveys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.qsurveys.QSurveyQuestion;
import sbs.repository.GenericRepository;
import sbs.repository.qsurveys.QSurveyQuestionsRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class QSurveyQuestionsServiceImpl extends GenericServiceAdapter<QSurveyQuestion, Integer> implements QSurveyQuestionsService{
	
	private QSurveyQuestionsRepository qSurveyQuestionsRepository;

    @Autowired
	public QSurveyQuestionsServiceImpl(@Qualifier("qSurveyQuestionsRepositoryImpl") GenericRepository<QSurveyQuestion, Integer> genericRepository) {
			super(genericRepository);
			this.qSurveyQuestionsRepository = (QSurveyQuestionsRepository) genericRepository;
	}

	@Override
	public List<QSurveyQuestion> findListByTemplateIdAsc(int templateId) {
		return qSurveyQuestionsRepository.findListByTemplateIdAsc(templateId);
	}


}

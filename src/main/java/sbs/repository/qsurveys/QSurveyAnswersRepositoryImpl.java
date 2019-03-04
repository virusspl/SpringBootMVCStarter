package sbs.repository.qsurveys;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.qsurveys.QSurveyAnswer;
import sbs.repository.GenericRepositoryAdapter;

@Repository("qSurveyAnswersRepositoryImpl")
@Transactional
public class QSurveyAnswersRepositoryImpl extends GenericRepositoryAdapter<QSurveyAnswer, Integer>
		implements QSurveyAnswersRepository {
	
	
}

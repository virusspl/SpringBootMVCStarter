package sbs.repository.qsurveys;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.qsurveys.QSurveyQuestion;
import sbs.repository.GenericRepositoryAdapter;

@Repository("qSurveyQuestionsRepositoryImpl")
@Transactional
public class QSurveyQuestionsRepositoryImpl extends GenericRepositoryAdapter<QSurveyQuestion, Integer>
		implements QSurveyQuestionsRepository {

	
}

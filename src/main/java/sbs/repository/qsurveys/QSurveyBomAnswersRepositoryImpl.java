package sbs.repository.qsurveys;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.qsurveys.QSurveyBomAnswer;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class QSurveyBomAnswersRepositoryImpl extends GenericRepositoryAdapter<QSurveyBomAnswer, Integer>
		implements QSurveyBomAnswersRepository {
	
	
}

package sbs.repository.qsurveys;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.qsurveys.QSurveyQuestionType;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class QSurveyQuestionTypesRepositoryImpl extends GenericRepositoryAdapter<QSurveyQuestionType, Integer>
		implements QSurveyQuestionTypesRepository {

	
}

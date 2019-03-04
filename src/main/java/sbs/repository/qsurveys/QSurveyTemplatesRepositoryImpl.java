package sbs.repository.qsurveys;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.qsurveys.QSurveyTemplate;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class QSurveyTemplatesRepositoryImpl extends GenericRepositoryAdapter<QSurveyTemplate, Integer>
		implements QSurveyTemplatesRepository {

	
}
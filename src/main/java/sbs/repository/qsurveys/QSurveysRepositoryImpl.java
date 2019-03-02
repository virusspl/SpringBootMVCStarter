package sbs.repository.qsurveys;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.qsurveys.QSurvey;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class QSurveysRepositoryImpl extends GenericRepositoryAdapter<QSurvey, Integer>
		implements QSurveysRepository {

	
}

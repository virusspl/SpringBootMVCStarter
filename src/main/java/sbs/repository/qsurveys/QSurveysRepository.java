package sbs.repository.qsurveys;

import java.util.List;

import sbs.model.qsurveys.QSurvey;
import sbs.repository.GenericRepository;

public interface QSurveysRepository extends GenericRepository<QSurvey,Integer> {

	List<QSurvey> findAllSortByDateDesc();
	
}


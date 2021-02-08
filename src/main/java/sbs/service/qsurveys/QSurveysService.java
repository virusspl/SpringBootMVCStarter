package sbs.service.qsurveys;

import java.util.Date;
import java.util.List;

import sbs.model.qsurveys.QSurvey;
import sbs.service.GenericService;

public interface QSurveysService extends GenericService<QSurvey, Integer>{

	List<QSurvey> findAllSortByDateDesc();
	List<QSurvey> findInPeriodSortByDateDesc(Date startDate, Date endDate);
	
	
}

package sbs.service.qsurveys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.qsurveys.QSurvey;
import sbs.repository.GenericRepository;
import sbs.repository.qsurveys.QSurveysRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class QSurveysServiceImpl extends GenericServiceAdapter<QSurvey, Integer> implements QSurveysService{
	
	private QSurveysRepository qSurveysRepository;
	
    @Autowired
	public QSurveysServiceImpl(@Qualifier("qSurveysRepositoryImpl") GenericRepository<QSurvey, Integer> genericRepository) {
			super(genericRepository);
			this.qSurveysRepository = (QSurveysRepository) genericRepository;
	}

	@Override
	public List<QSurvey> findAllSortByDateDesc() {
		return qSurveysRepository.findAllSortByDateDesc();
	}

}

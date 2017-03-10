package sbs.service.qualitysurveys;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.qualitysurveys.QualitySurvey;
import sbs.repository.GenericRepository;
import sbs.repository.qualitysurveys.QualitySurveysRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class QualitySurveysServiceImpl extends GenericServiceAdapter<QualitySurvey, Integer> implements QualitySurveysService{
	
	private QualitySurveysRepository qualitySurveysRepository;
	
    @Autowired
	public QualitySurveysServiceImpl(@Qualifier("qualitySurveysRepositoryImpl") GenericRepository<QualitySurvey, Integer> genericRepository) {
			super(genericRepository);
			this.qualitySurveysRepository = (QualitySurveysRepository) genericRepository;
	}

	@Override
	public List<QualitySurvey> findAllDesc() {
		List<QualitySurvey> list = qualitySurveysRepository.findAllDesc(); 
		for (QualitySurvey survey: list){
			Hibernate.initialize(survey.getUser());
		}
		return list;
	}


	

}

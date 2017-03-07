package sbs.service.qualitysurveys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.qualitysurveys.QualitySurveyParameter;
import sbs.repository.GenericRepository;
import sbs.repository.qualitysurveys.QualitySurveyParametersRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class QualitySurveyParametersServiceImpl extends GenericServiceAdapter<QualitySurveyParameter, Integer> implements QualitySurveyParametersService{
	
	private QualitySurveyParametersRepository qualitySurveyParametersRepository;
	
    @Autowired
	public QualitySurveyParametersServiceImpl(@Qualifier("qualitySurveyParametersRepositoryImpl") GenericRepository<QualitySurveyParameter, Integer> genericRepository) {
			super(genericRepository);
			this.qualitySurveyParametersRepository = (QualitySurveyParametersRepository) genericRepository;
	}

    public List<QualitySurveyParameter> findAllActive(){
    	return this.qualitySurveyParametersRepository.findAllActive();
    }
	

}

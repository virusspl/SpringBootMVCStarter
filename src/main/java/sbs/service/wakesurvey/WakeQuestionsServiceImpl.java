package sbs.service.wakesurvey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.wakesurvey.WakeQuestion;
import sbs.repository.GenericRepository;
import sbs.repository.wakesurvey.WakeQuestionsRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class WakeQuestionsServiceImpl extends GenericServiceAdapter<WakeQuestion, Integer> implements WakeQuestionsService{
	
	@SuppressWarnings("unused")
	private WakeQuestionsRepository wakeQuestionsRepository;
	
    @Autowired
	public WakeQuestionsServiceImpl(@Qualifier("wakeQuestionsRepositoryImpl") GenericRepository<WakeQuestion, Integer> genericRepository) {
			super(genericRepository);
			this.wakeQuestionsRepository = (WakeQuestionsRepository) genericRepository;
	}

}
package sbs.service.qcheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.qcheck.QCheckState;
import sbs.repository.GenericRepository;
import sbs.repository.qcheck.QCheckStatesRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class QCheckStatesServiceImpl extends GenericServiceAdapter<QCheckState, Integer> implements QCheckStatesService{
	
	@SuppressWarnings("unused")
	private QCheckStatesRepository qCheckStatesRepository;
	
    @Autowired
	public QCheckStatesServiceImpl(@Qualifier("qCheckStatesRepositoryImpl") GenericRepository<QCheckState, Integer> genericRepository) {
			super(genericRepository);
			this.qCheckStatesRepository = (QCheckStatesRepository) genericRepository;
	}


}

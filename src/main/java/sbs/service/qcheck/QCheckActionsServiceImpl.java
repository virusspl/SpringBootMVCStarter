package sbs.service.qcheck;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.qcheck.QCheckAction;
import sbs.repository.GenericRepository;
import sbs.repository.qcheck.QCheckActionsRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class QCheckActionsServiceImpl extends GenericServiceAdapter<QCheckAction, Integer> implements QCheckActionsService{
	
	private QCheckActionsRepository qCheckActionsRepository;
	
    @Autowired
	public QCheckActionsServiceImpl(@Qualifier("qCheckActionsRepositoryImpl") GenericRepository<QCheckAction, Integer> genericRepository) {
			super(genericRepository);
			this.qCheckActionsRepository = (QCheckActionsRepository) genericRepository;
	}

	@Override
	public List<QCheckAction> findByCheckId(int id) {
		return qCheckActionsRepository.findByCheckId(id);
	}
    
}

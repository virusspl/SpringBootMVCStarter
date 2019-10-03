package sbs.repository.compreq;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.compreq.ComponentRequestLine;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class ComponentRequestLinesRepositoryImpl extends GenericRepositoryAdapter<ComponentRequestLine, Integer>
		implements ComponentRequestLinesRepository {


	
}



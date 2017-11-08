package sbs.repository.wakesurvey;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.wakesurvey.WakeQuestion;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class WakeQuestionsRepositoryImpl extends GenericRepositoryAdapter<WakeQuestion, Integer>
		implements WakeQuestionsRepository {

}
package sbs.repository.qsurveys;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.qsurveys.QSurveyTemplate;
import sbs.repository.GenericRepositoryAdapter;

@Repository("qSurveyTemplatesRepositoryImpl")
@Transactional
public class QSurveyTemplatesRepositoryImpl extends GenericRepositoryAdapter<QSurveyTemplate, Integer>
		implements QSurveyTemplatesRepository {

	@Override
	public List<QSurveyTemplate> findAllActiveAscByTitle() {
		String hql = "from QSurveyTemplate t WHERE active = :boolVal ORDER BY title ASC";
		@SuppressWarnings("unchecked")
		List<QSurveyTemplate> result = (List<QSurveyTemplate>) currentSession().createQuery(hql).setBoolean("boolVal", true).list();
		return result;
	}

	
}

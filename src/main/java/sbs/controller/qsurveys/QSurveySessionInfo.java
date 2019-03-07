package sbs.controller.qsurveys;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import sbs.model.hr.HrUserInfo;
import sbs.model.qsurveys.QSurveyQuestionType;
import sbs.model.qsurveys.QSurveyTemplate;

@Component
@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class QSurveySessionInfo {
	
	private List<HrUserInfo> workers;
	private List<QSurveyTemplate> templates;
	private List<QSurveyQuestionType> types;
	
	public QSurveySessionInfo() {
		workers = new ArrayList<>();
		templates = new ArrayList<>();
		types = new ArrayList<>();
	}

	public List<HrUserInfo> getWorkers() {
		return workers;
	}

	public void setWorkers(List<HrUserInfo> workers) {
		this.workers = workers;
	}

	public List<QSurveyTemplate> getTemplates() {
		return templates;
	}

	public void setTemplates(List<QSurveyTemplate> templates) {
		this.templates = templates;
	}

	public List<QSurveyQuestionType> getTypes() {
		return types;
	}

	public void setTypes(List<QSurveyQuestionType> types) {
		this.types = types;
	}
	

	
	
	
	
	
	

	
}
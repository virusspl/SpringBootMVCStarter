package sbs.model.tools;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tools_project_states")
public class ToolsProjectState {
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "state")
	Set<ToolsProject> toolsProjects;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "tps_id")
	private int id;
	
	@Column(name = "tps_description", length = 35, nullable = false)
	private String description;
	
	@Column(name = "tps_order")
	private int order;

	
	public ToolsProjectState() {
		toolsProjects = new HashSet<>();
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public int getOrder() {
		return order;
	}


	public void setOrder(int order) {
		this.order = order;
	}


	public Set<ToolsProject> getToolsProjects() {
		return toolsProjects;
	}


	public void setToolsProjects(Set<ToolsProject> toolsProjects) {
		this.toolsProjects = toolsProjects;
	}


	@Override
	public String toString() {
		return "ToolsProjectState [toolsProjects=" + toolsProjects + ", id=" + id + ", description=" + description
				+ ", order=" + order + "]";
	}
	
	
	
	
	


}

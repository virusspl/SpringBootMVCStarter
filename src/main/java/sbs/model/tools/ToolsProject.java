package sbs.model.tools;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import sbs.model.users.User;

@Entity
@Table(name = "tools_project")
public class ToolsProject {
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tp_state_id", nullable = false)
	private ToolsProjectState state;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tp_usr_id_creuser", nullable = false)
	private User creator;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tp_usr_id_assigned_user", nullable = true)
	private User assignedUser;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "tp_id")
	private int id;
	
	@Column(name = "tp_cech_old", length = 30, nullable = true)
	private String cechOld;
	
	@Column(name = "tp_cech_new", length = 30, nullable = true)
	private String cechNew;
		
	@Column(name = "tp_asset_code", length = 30, nullable = true)
	private String assetCode;
	
	@Column(name = "tp_asset_name", length = 100, nullable = true)
	private String assetName;
	
	@Column(name = "tp_client_code", length = 15, nullable = true)
	private String clientCode;
	
	@Column(name = "tp_client_name", length = 70, nullable = true)
	private String clientName;
	
	@Column(name = "tp_thumbnail_filename", length = 50, nullable = true)
	private String thumbnailFileName;
	
	@Column(name = "tp_description", nullable = false)
	@Type(type="text")
	private String description;

	@Column(name = "tp_priority", nullable = true)
	private int priority;

	@Column(name = "tp_credat", nullable = false)
	private Timestamp creationDate;

	@Column(name = "tp_lastupddat", nullable = false)
	private Timestamp updateDate;	
	
	public ToolsProject() {

	}

	
	
	

}

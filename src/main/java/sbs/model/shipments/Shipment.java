package sbs.model.shipments;

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
@Table(name = "shipments")
public class Shipment {	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sh_shipment_id")
	private int id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sh_usr_id_creator", nullable = false)
	private User creator;	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sh_ss_id_shipment_state", nullable = false)
	private ShipmentState state;
	 
	@Column(name = "sh_credat", nullable = false)
	private Timestamp creationDate;

	@Column(name = "sh_lastupddat", nullable = false)
	private Timestamp updateDate;	

	@Column(name = "sh_company", length = 10, nullable = false)
	private String company;

	@Column(name = "sh_client_code", length = 10, nullable = false)
	private String clientCode;
	
	@Column(name = "sh_client_name", length = 90, nullable = false)
	private String clientName;
	
	@Column(name = "sh_client_country", length = 5, nullable = false)
	private String clientCountry;
	
	@Column(name = "sh_description", nullable = true)
	@Type(type="text")
	private String description;

	
	public Shipment() {

	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public User getCreator() {
		return creator;
	}


	public void setCreator(User creator) {
		this.creator = creator;
	}


	public ShipmentState getState() {
		return state;
	}


	public void setState(ShipmentState state) {
		this.state = state;
	}


	public Timestamp getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}


	public Timestamp getUpdateDate() {
		return updateDate;
	}


	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}


	public String getCompany() {
		return company;
	}


	public void setCompany(String company) {
		this.company = company;
	}


	public String getClientCode() {
		return clientCode;
	}


	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}


	public String getClientName() {
		return clientName;
	}


	public void setClientName(String clientName) {
		this.clientName = clientName;
	}


	public String getClientCountry() {
		return clientCountry;
	}


	public void setClientCountry(String clientCountry) {
		this.clientCountry = clientCountry;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	@Override
	public String toString() {
		return "Shipment [id=" + id + ", state=" + state.getInternalTitle() + ", creationDate=" + creationDate + ", updateDate="
				+ updateDate + ", company=" + company + ", clientCode=" + clientCode + ", clientName=" + clientName
				+ ", clientCountry=" + clientCountry + ", description=" + description + "]";
	}


}

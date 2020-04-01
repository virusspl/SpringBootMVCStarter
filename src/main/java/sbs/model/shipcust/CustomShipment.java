package sbs.model.shipcust;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import sbs.model.users.User;

@Entity
@Table(name = "shipcust_shipment")
public class CustomShipment {
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "scsh_creation_user_id", nullable = false)
	private User creator;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "scsh_state_id", nullable = false)
	private ShipCustState state;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "scsh_transport_id", nullable = false)
	private ShipCustTransport transport;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "shipment")
	private Set<CustomShipmentLine> lines;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "scsh_shipment_id")
	private int id;
	
	@Column(name = "scsh_client_code", length = 10, nullable = false)
	private String clientCode;
	
	@Column(name = "scsh_client_name", length = 150, nullable = false)
	private String clientName;
	
	@Column(name = "scsh_comment", length = 255, nullable = true)
	private String comment;
	
	@Column(name = "scsh_creation_date", nullable = false)
	private Timestamp creationDate;
	
	@Column(name = "scsh_close_date", nullable = true)
	private Timestamp closeDate;
	
	@Column(name = "scsh_update_date", nullable = false)
	private Timestamp updateDate;
	
	@Column(name = "scsh_start_date", nullable = false)
	private Timestamp startDate;
	
	@Column(name = "scsh_end_date", nullable = false)
	private Timestamp endDate;

	public CustomShipment() {

	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public ShipCustState getState() {
		return state;
	}

	public void setState(ShipCustState state) {
		this.state = state;
	}

	public ShipCustTransport getTransport() {
		return transport;
	}

	public void setTransport(ShipCustTransport transport) {
		this.transport = transport;
	}

	public Set<CustomShipmentLine> getLines() {
		return lines;
	}

	public void setLines(Set<CustomShipmentLine> lines) {
		this.lines = lines;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public Timestamp getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Timestamp closeDate) {
		this.closeDate = closeDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "CustomShipment [creator=" + creator + ", state=" + state + ", transport=" + transport + ", id=" + id
				+ ", clientCode=" + clientCode + ", clientName=" + clientName + ", comment=" + comment
				+ ", creationDate=" + creationDate + ", closeDate=" + closeDate + ", updateDate=" + updateDate
				+ ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}

	
	
}

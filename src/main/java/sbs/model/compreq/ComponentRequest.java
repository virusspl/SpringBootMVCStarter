package sbs.model.compreq;

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
@Table(name = "component_requests")
public class ComponentRequest {

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "request")
	Set<ComponentRequestLine> requestLines;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cr_request_id")
	private int id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cr_creator_id", nullable = false)
	private User creator;
	
	@Column(name = "cr_start_date", nullable = false)
	private Timestamp startDate;
	
	@Column(name = "cr_end_date", nullable = true)
	private Timestamp endDate;	
	
	@Column(name = "cr_product_code", length = 25, nullable = false)
	private String productCode;
	
	@Column(name = "cr_product_description", length = 150, nullable = false)
	private String productDescription;
	
	@Column(name = "cr_order_number", length = 25, nullable = false)
	private String orderNumber;
	
	@Column(name = "cr_client_code", length = 15, nullable = false)
	private String clientCode;
	;
	@Column(name = "cr_client_name", length = 100, nullable = false)
	private String clientName;
	
	@Column(name = "cr_workstation_code", length = 15, nullable = false)
	private String workstationCode;
	
	@Column(name = "cr_workstation_name", length = 75, nullable = false)
	private String workstationName;

	@Column(name = "cr_pending", nullable = false )
	private boolean pending;

	@Column(name = "cr_comment", length = 255, nullable = true)
	private String comment;

	
	public ComponentRequest() {
	
	}


	public Set<ComponentRequestLine> getRequestLines() {
		return requestLines;
	}


	public void setRequestLines(Set<ComponentRequestLine> requestLines) {
		this.requestLines = requestLines;
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


	public String getProductCode() {
		return productCode;
	}


	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}


	public String getProductDescription() {
		return productDescription;
	}


	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}


	public String getOrderNumber() {
		return orderNumber;
	}


	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
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


	public String getWorkstationCode() {
		return workstationCode;
	}


	public void setWorkstationCode(String workstationCode) {
		this.workstationCode = workstationCode;
	}


	public String getWorkstationName() {
		return workstationName;
	}


	public void setWorkstationName(String workstationName) {
		this.workstationName = workstationName;
	}


	public boolean isPending() {
		return pending;
	}


	public void setPending(boolean pending) {
		this.pending = pending;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	@Override
	public String toString() {
		return "ComponentRequest [id=" + id + ", creator=" + creator.getName() + ", startDate="
				+ startDate + ", endDate=" + endDate + ", productCode=" + productCode + ", productDescription="
				+ productDescription + ", orderNumber=" + orderNumber + ", clientCode=" + clientCode + ", clientName="
				+ clientName + ", workstationCode=" + workstationCode + ", workstationName=" + workstationName
				+ ", pending=" + pending + ", comment=" + comment + "]";
	}
	

	
	

}

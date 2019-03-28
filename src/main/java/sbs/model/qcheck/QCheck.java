package sbs.model.qcheck;

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
@Table(name = "qchecks")
public class QCheck {
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "qc_creation_user_id", nullable = false)
	private User creator;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "qc_assigned_user_id", nullable = true)
	private User assignedUser;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "qc_state_id", nullable = false)
	private QCheckState state;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "qc_check_id")
	private int id;
	
	@Column(name = "qc_creation_date", nullable = false)
	private Timestamp creationDate;
	
	@Column(name = "qc_start_date", nullable = true)
	private Timestamp startDate;
	
	@Column(name = "qc_end_date", nullable = true)
	private Timestamp endDate;
	
	@Column(name = "qc_product_code", length = 30, nullable = false)
	private String productCode;
	
	@Column(name = "qc_product_description", length = 150, nullable = false)
	private String productDescription;
	
	@Column(name = "qc_contents")
	@Type(type="text")
	private String contents;
	
	@Column(name = "qc_result")
	@Type(type="text")
	private String result;

	
	public QCheck() {

	}


	public User getCreator() {
		return creator;
	}


	public void setCreator(User creator) {
		this.creator = creator;
	}


	public User getAssignedUser() {
		return assignedUser;
	}


	public void setAssignedUser(User assignedUser) {
		this.assignedUser = assignedUser;
	}


	public QCheckState getState() {
		return state;
	}


	public void setState(QCheckState state) {
		this.state = state;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Timestamp getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
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


	public String getContents() {
		return contents;
	}


	public void setContents(String contents) {
		this.contents = contents;
	}


	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}


	@Override
	public String toString() {
		return "QCheck [id=" + id + ", creationDate=" + creationDate + ", startDate=" + startDate + ", endDate="
				+ endDate + ", productCode=" + productCode + ", productDescription=" + productDescription
				+ ", contents=" + contents + ", result=" + result + "]";
	}
	
	
	
	
	
	
}

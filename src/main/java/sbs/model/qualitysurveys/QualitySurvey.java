package sbs.model.qualitysurveys;

import java.sql.Timestamp;
import java.util.HashSet;
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
@Table(name = "quality_surveys")
public class QualitySurvey {

	public static final String QUALITY_SURVEY_TYPE_PARAM = "param";
	public static final String QUALITY_SURVEY_TYPE_BOM = "bom";
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qs_user_id", nullable = false)
	private User user;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "survey")
	Set<QualitySurveyParameterAnswer> parameterAnswers;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "survey")
	Set<QualitySurveyBomAnswer> bomAnswers;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "qs_id")
	private int id;

	@Column(name = "qs_credat", nullable = false)
	private Timestamp creationDate;

	@Column(name = "qs_operator_id", length = 10, nullable = false)
	private String operatorId;

	
	@Column(name = "qs_operator_rcp_no", length = 15, nullable = false)
	private String operatorRcpNo;

	
	@Column(name = "qs_operator_last_name", length = 55, nullable = false)
	private String operatorLastName;

	
	@Column(name = "qs_operator_first_name", length = 35, nullable = false)
	private String operatorFirstName;

	
	@Column(name = "qs_operator_department", length = 90, nullable = false)
	private String operatorDepartment;

	
	@Column(name = "qs_operator_position", length = 120, nullable = false)
	private String operatorPosition;

	
	@Column(name = "qs_product_code", length = 25, nullable = false)
	private String productCode;

	
	@Column(name = "qs_product_description", length = 100, nullable = false)
	private String productDescription;

	
	@Column(name = "qs_production_order", length = 20,  nullable = false)
	private String productionOrder;

	
	@Column(name = "qs_client_code", length = 10, nullable = false)
	private String clientCode;

	
	@Column(name = "qs_client_name", length = 120, nullable = false)
	private String clientName;

	
	@Column(name = "qs_client_order", length = 25, nullable = false)
	private String salesOrder;

	@Column(name = "qs_type", length = 15, nullable = false)
	private String type;
	
	public QualitySurvey() {
		 parameterAnswers = new HashSet<>();
		 bomAnswers = new HashSet<>();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorRcpNo() {
		return operatorRcpNo;
	}

	public void setOperatorRcpNo(String operatorRcpNo) {
		this.operatorRcpNo = operatorRcpNo;
	}

	public String getOperatorLastName() {
		return operatorLastName;
	}

	public void setOperatorLastName(String operatorLastName) {
		this.operatorLastName = operatorLastName;
	}

	public String getOperatorFirstName() {
		return operatorFirstName;
	}

	public void setOperatorFirstName(String operatorFirstName) {
		this.operatorFirstName = operatorFirstName;
	}

	public String getOperatorDepartment() {
		return operatorDepartment;
	}

	public void setOperatorDepartment(String operatorDepartment) {
		this.operatorDepartment = operatorDepartment;
	}

	public String getOperatorPosition() {
		return operatorPosition;
	}

	public void setOperatorPosition(String operatorPosition) {
		this.operatorPosition = operatorPosition;
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

	public String getProductionOrder() {
		return productionOrder;
	}

	public void setProductionOrder(String productionOrder) {
		this.productionOrder = productionOrder;
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

	public String getSalesOrder() {
		return salesOrder;
	}

	public void setSalesOrder(String salesOrder) {
		this.salesOrder = salesOrder;
	}

	public Set<QualitySurveyParameterAnswer> getParameterAnswers() {
		return parameterAnswers;
	}

	public void setParameterAnswers(Set<QualitySurveyParameterAnswer> parameterAnswers) {
		this.parameterAnswers = parameterAnswers;
	}

	public Set<QualitySurveyBomAnswer> getBomAnswers() {
		return bomAnswers;
	}

	public void setBomAnswers(Set<QualitySurveyBomAnswer> bomAnswers) {
		this.bomAnswers = bomAnswers;
	}
	
	

}

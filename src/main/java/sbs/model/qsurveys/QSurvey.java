package sbs.model.qsurveys;

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

import org.hibernate.annotations.Type;

import sbs.model.users.User;

@Entity
@Table(name = "qsurvey_surveys")
public class QSurvey {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qs_auth_user_id", nullable = false)
	private User user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "qs_template_id", nullable = false)
	private QSurveyTemplate template;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "survey")
	Set<QSurveyBomAnswer> bomAnswers;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "survey")
	Set<QSurveyAnswer> answers;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "qs_survey_id")
	private int id;

	@Column(name = "qs_credat", nullable = false)
	private Timestamp creationDate;

	@Column(name = "qs_template_date", nullable = true)
	private Timestamp templateSurveyDate;
	
	@Column(name = "qs_bom_date", nullable = true)
	private Timestamp bomSurveyDate;
	
	@Column(name = "qs_comment", nullable = true)
	@Type(type="text")
	private String comment;
	
	@Column(name = "qs_operator_manual", nullable = false)
	private Boolean operatorManual;
	
	@Column(name = "qs_operator_number", length = 10, nullable = false)
	private String operatorNumber;
	
	@Column(name = "qs_operator_first_name", length = 45, nullable = false)
	private String operatorFirstName;
	
	@Column(name = "qs_operator_last_name", length = 65, nullable = false)
	private String operatorLastName;
	
	@Column(name = "qs_operator_department", length = 75, nullable = false)
	private String operatorDepartment;
	
	@Column(name = "qs_operator_position", length = 55, nullable = false)
	private String operatorPosition;
	
	@Column(name = "qs_order_number", length = 30, nullable = false)
	private String orderNumber;
	
	@Column(name = "qs_order_operation", length = 10, nullable = false)
	private String orderOperation;
	
	@Column(name = "qs_order_product", length = 30, nullable = false)
	private String orderProduct;
	
	@Column(name = "qs_order_product_description", length = 100, nullable = false)
	private String orderProductDescription;
	
	@Column(name = "qs_order_client_code", length = 15, nullable = false)
	private String orderClientCode;
	
	@Column(name = "qs_order_client_name", length = 70, nullable = false)
	private String orderClientName;

	@Column(name = "qs_order_quantity_ordered", nullable = false)
	private int orderQuantityOrdered;
	
	@Column(name = "qs_quantity_checked", nullable = false)
	private int orderQuantityChecked;
	
	public QSurvey() {
	
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public QSurveyTemplate getTemplate() {
		return template;
	}

	public void setTemplate(QSurveyTemplate template) {
		this.template = template;
	}

	public Set<QSurveyBomAnswer> getBomAnswers() {
		return bomAnswers;
	}

	public void setBomAnswers(Set<QSurveyBomAnswer> bomAnswers) {
		this.bomAnswers = bomAnswers;
	}

	public Set<QSurveyAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<QSurveyAnswer> answers) {
		this.answers = answers;
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

	public Timestamp getTemplateSurveyDate() {
		return templateSurveyDate;
	}

	public void setTemplateSurveyDate(Timestamp templateSurveyDate) {
		this.templateSurveyDate = templateSurveyDate;
	}

	public Timestamp getBomSurveyDate() {
		return bomSurveyDate;
	}

	public void setBomSurveyDate(Timestamp bomSurveyDate) {
		this.bomSurveyDate = bomSurveyDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Boolean getOperatorManual() {
		return operatorManual;
	}

	public void setOperatorManual(Boolean operatorManual) {
		this.operatorManual = operatorManual;
	}

	public String getOperatorNumber() {
		return operatorNumber;
	}

	public void setOperatorNumber(String operatorNumber) {
		this.operatorNumber = operatorNumber;
	}

	public String getOperatorFirstName() {
		return operatorFirstName;
	}

	public void setOperatorFirstName(String operatorFirstName) {
		this.operatorFirstName = operatorFirstName;
	}

	public String getOperatorLastName() {
		return operatorLastName;
	}

	public void setOperatorLastName(String operatorLastName) {
		this.operatorLastName = operatorLastName;
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

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderOperation() {
		return orderOperation;
	}

	public void setOrderOperation(String orderOperation) {
		this.orderOperation = orderOperation;
	}

	public String getOrderProduct() {
		return orderProduct;
	}

	public void setOrderProduct(String orderProduct) {
		this.orderProduct = orderProduct;
	}

	public String getOrderProductDescription() {
		return orderProductDescription;
	}

	public void setOrderProductDescription(String orderProductDescription) {
		this.orderProductDescription = orderProductDescription;
	}

	public String getOrderClientCode() {
		return orderClientCode;
	}

	public void setOrderClientCode(String orderClientCode) {
		this.orderClientCode = orderClientCode;
	}

	public String getOrderClientName() {
		return orderClientName;
	}

	public void setOrderClientName(String orderClientName) {
		this.orderClientName = orderClientName;
	}

	public int getOrderQuantityOrdered() {
		return orderQuantityOrdered;
	}

	public void setOrderQuantityOrdered(int orderQuantityOrdered) {
		this.orderQuantityOrdered = orderQuantityOrdered;
	}

	public int getOrderQuantityChecked() {
		return orderQuantityChecked;
	}

	public void setOrderQuantityChecked(int orderQuantityChecked) {
		this.orderQuantityChecked = orderQuantityChecked;
	}

	@Override
	public String toString() {
		return "QSurvey [id=" + id + ", creationDate=" + creationDate + "]";
	}

	
	
	
}

package sbs.model.proprog;

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

import sbs.model.users.User;

@Entity
@Table(name = "project_progress")
public class Project {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pp_id")
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pp_project_number_user", nullable = false)
	private User projectNumberUser;
	@Column(name = "pp_project_number", length = 30, nullable = false)
	private String projectNumber;
	@Column(name = "pp_project_number_date", nullable = false)
	private Timestamp projectNumberDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pp_client_accept_user", nullable = true)
	private User clientAcceptUser;
	@Column(name = "pp_client_accept_date", nullable = true)
	private Timestamp clientAcceptDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pp_codification_user", nullable = true)
	private User codificationUser;
	@Column(name = "pp_codification_date", nullable = true)
	private Timestamp codificationDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pp_drawing_validation_user", nullable = true)
	private User drawingValidationUser;
	@Column(name = "pp_drawing_validation_date", nullable = true)
	private Timestamp drawingValidationDate;
	@Column(name = "pp_product_code", length = 30, nullable = true)
	private String drawingNumber;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pp_order_input_user", nullable = true)
	private User orderInputUser;
	@Column(name = "pp_order_input_date", nullable = true)
	private Timestamp orderInputDate;
	@Column(name = "pp_order_number", length = 30, nullable = true)
	private String orderNumber;
	@Column(name = "pp_order_quantity", nullable = false)
	private int orderQuantity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pp_production_plan_user", nullable = true)
	private User productionPlanUser;
	@Column(name = "pp_production_plan_date", nullable = true)
	private Timestamp productionPlanDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pp_buy_parts_user", nullable = true)
	private User buyPartsUser;
	@Column(name = "pp_buy_parts_date", nullable = true)
	private Timestamp buyPartsDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pp_technology_user", nullable = true)
	private User technologyUser;
	@Column(name = "pp_technology_date", nullable = true)
	private Timestamp technologyDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pp_tool_drawing_user", nullable = true)
	private User toolDrawingUser;
	@Column(name = "pp_tool_drawing_date", nullable = true)
	private Timestamp toolDrawingDate;
	@Column(name = "pp_tool_drawing_needed", nullable = true)
	private boolean toolDrawingNeeded;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pp_tool_creation_user", nullable = true)
	private User toolCreationUser;
	@Column(name = "pp_tool_creation_date", nullable = true)
	private Timestamp toolCreationDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pp_first_item_user", nullable = true)
	private User firstItemUser;
	@Column(name = "pp_first_item_date", nullable = true)
	private Timestamp firstItemDate;
	
	public Project() {
		 
	}

	public int getProgressTotal(){
		// project number exists
		int progress = 0;
		// other steps
		progress = (this.clientAcceptUser == null ? progress : (progress+10));
		progress = (this.codificationUser == null ? progress : (progress+10));
		progress = (this.drawingValidationUser == null ? progress : (progress+10));
		progress = (this.orderInputUser == null ? progress : (progress+10));
		progress = (this.productionPlanUser == null ? progress : (progress+10));
		progress = (this.buyPartsUser == null ? progress : (progress+10));
		progress = (this.technologyUser == null ? progress : (progress+10));
		progress = (this.toolDrawingUser == null ? progress : (progress+10));
		progress = (this.toolCreationUser == null ? progress : (progress+10));
		progress = (this.firstItemUser == null ? progress : (progress+10));
		return progress;
	}
	
	/*
	 * returns bootstrap class name
	 * * danger
	 * * warning
	 * * info
	 * * success
	 */
	public String getProgressBootstrapTitle(){
			
		if (this.getProgressTotal() < 21){
			return "danger";
		}
		else if (this.getProgressTotal() < 51){
			return "warning";
		}
		else if (this.getProgressTotal() < 81){
			return "info";
		}
		else{
			return "success";
		}
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getProjectNumberUser() {
		return projectNumberUser;
	}

	public void setProjectNumberUser(User projectNumberUser) {
		this.projectNumberUser = projectNumberUser;
	}

	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}

	public Timestamp getProjectNumberDate() {
		return projectNumberDate;
	}

	public void setProjectNumberDate(Timestamp projectNumberDate) {
		this.projectNumberDate = projectNumberDate;
	}

	public User getClientAcceptUser() {
		return clientAcceptUser;
	}

	public void setClientAcceptUser(User clientAcceptUser) {
		this.clientAcceptUser = clientAcceptUser;
	}

	public Timestamp getClientAcceptDate() {
		return clientAcceptDate;
	}

	public void setClientAcceptDate(Timestamp clientAcceptDate) {
		this.clientAcceptDate = clientAcceptDate;
	}

	public User getCodificationUser() {
		return codificationUser;
	}

	public void setCodificationUser(User codificationUser) {
		this.codificationUser = codificationUser;
	}

	public Timestamp getCodificationDate() {
		return codificationDate;
	}

	public void setCodificationDate(Timestamp codificationDate) {
		this.codificationDate = codificationDate;
	}

	public User getDrawingValidationUser() {
		return drawingValidationUser;
	}

	public void setDrawingValidationUser(User drawingValidationUser) {
		this.drawingValidationUser = drawingValidationUser;
	}

	public Timestamp getDrawingValidationDate() {
		return drawingValidationDate;
	}

	public void setDrawingValidationDate(Timestamp drawingValidationDate) {
		this.drawingValidationDate = drawingValidationDate;
	}

	public String getDrawingNumber() {
		return drawingNumber;
	}

	public void setDrawingNumber(String drawingNumber) {
		this.drawingNumber = drawingNumber;
	}

	public User getOrderInputUser() {
		return orderInputUser;
	}

	public void setOrderInputUser(User orderInputUser) {
		this.orderInputUser = orderInputUser;
	}

	public Timestamp getOrderInputDate() {
		return orderInputDate;
	}

	public void setOrderInputDate(Timestamp orderInputDate) {
		this.orderInputDate = orderInputDate;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(int orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public User getProductionPlanUser() {
		return productionPlanUser;
	}

	public void setProductionPlanUser(User productionPlanUser) {
		this.productionPlanUser = productionPlanUser;
	}

	public Timestamp getProductionPlanDate() {
		return productionPlanDate;
	}

	public void setProductionPlanDate(Timestamp productionPlanDate) {
		this.productionPlanDate = productionPlanDate;
	}

	public User getBuyPartsUser() {
		return buyPartsUser;
	}

	public void setBuyPartsUser(User buyPartsUser) {
		this.buyPartsUser = buyPartsUser;
	}

	public Timestamp getBuyPartsDate() {
		return buyPartsDate;
	}

	public void setBuyPartsDate(Timestamp buyPartsDate) {
		this.buyPartsDate = buyPartsDate;
	}

	public User getTechnologyUser() {
		return technologyUser;
	}

	public void setTechnologyUser(User technologyUser) {
		this.technologyUser = technologyUser;
	}

	public Timestamp getTechnologyDate() {
		return technologyDate;
	}

	public void setTechnologyDate(Timestamp technologyDate) {
		this.technologyDate = technologyDate;
	}

	public User getToolDrawingUser() {
		return toolDrawingUser;
	}

	public void setToolDrawingUser(User toolDrawingUser) {
		this.toolDrawingUser = toolDrawingUser;
	}

	public Timestamp getToolDrawingDate() {
		return toolDrawingDate;
	}

	public void setToolDrawingDate(Timestamp toolDrawingDate) {
		this.toolDrawingDate = toolDrawingDate;
	}

	public boolean isToolDrawingNeeded() {
		return toolDrawingNeeded;
	}

	public void setToolDrawingNeeded(boolean toolDrawingNeeded) {
		this.toolDrawingNeeded = toolDrawingNeeded;
	}

	public User getToolCreationUser() {
		return toolCreationUser;
	}

	public void setToolCreationUser(User toolCreationUser) {
		this.toolCreationUser = toolCreationUser;
	}

	public Timestamp getToolCreationDate() {
		return toolCreationDate;
	}

	public void setToolCreationDate(Timestamp toolCreationDate) {
		this.toolCreationDate = toolCreationDate;
	}

	public User getFirstItemUser() {
		return firstItemUser;
	}

	public void setFirstItemUser(User firstItemUser) {
		this.firstItemUser = firstItemUser;
	}

	public Timestamp getFirstItemDate() {
		return firstItemDate;
	}

	public void setFirstItemDate(Timestamp firstItemDate) {
		this.firstItemDate = firstItemDate;
	}
	
	
	
	
}

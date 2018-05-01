package sbs.model.proprog;

import java.sql.Timestamp;

public class Project {
	
	private int id;
	
	private String projectNumber;
	private Timestamp projectNumberDate;
	private Timestamp clientAcceptDate;
	private Timestamp codificationDate;
	private Timestamp drawingValidationDate;
	private String drawingNumber;
	private Timestamp orderInputDate;
	private String orderNumber;
	private int orderQuantity;
	private Timestamp productionPlanDate;
	private Timestamp buyPartsDate;
	private Timestamp technologyDate;
	private Timestamp toolDrawingDate;
	private boolean toolDrawingNeeded;
	private Timestamp toolCreationDate;
	private Timestamp firstItemDate;
	
	public Project() {
		 
	}

	public int getProgressTotal(){
		// project number exists
		int progress = 0;
		// other steps
		progress = (this.clientAcceptDate == null ? progress : (progress+10));
		progress = (this.codificationDate == null ? progress : (progress+10));
		progress = (this.drawingValidationDate == null ? progress : (progress+10));
		progress = (this.orderInputDate == null ? progress : (progress+10));
		progress = (this.productionPlanDate == null ? progress : (progress+10));
		progress = (this.buyPartsDate == null ? progress : (progress+10));
		progress = (this.technologyDate == null ? progress : (progress+10));
		progress = (this.toolDrawingDate == null ? progress : (progress+10));
		progress = (this.toolCreationDate == null ? progress : (progress+10));
		progress = (this.firstItemDate == null ? progress : (progress+10));
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

	public Timestamp getClientAcceptDate() {
		return clientAcceptDate;
	}

	public void setClientAcceptDate(Timestamp clientAcceptDate) {
		this.clientAcceptDate = clientAcceptDate;
	}

	public Timestamp getCodificationDate() {
		return codificationDate;
	}

	public void setCodificationDate(Timestamp codificationDate) {
		this.codificationDate = codificationDate;
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

	public Timestamp getProductionPlanDate() {
		return productionPlanDate;
	}

	public void setProductionPlanDate(Timestamp productionPlanDate) {
		this.productionPlanDate = productionPlanDate;
	}

	public Timestamp getBuyPartsDate() {
		return buyPartsDate;
	}

	public void setBuyPartsDate(Timestamp buyPartsDate) {
		this.buyPartsDate = buyPartsDate;
	}

	public Timestamp getTechnologyDate() {
		return technologyDate;
	}

	public void setTechnologyDate(Timestamp technologyDate) {
		this.technologyDate = technologyDate;
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

	public Timestamp getToolCreationDate() {
		return toolCreationDate;
	}

	public void setToolCreationDate(Timestamp toolCreationDate) {
		this.toolCreationDate = toolCreationDate;
	}

	public Timestamp getFirstItemDate() {
		return firstItemDate;
	}

	public void setFirstItemDate(Timestamp firstItemDate) {
		this.firstItemDate = firstItemDate;
	}
	
	
	
	
}

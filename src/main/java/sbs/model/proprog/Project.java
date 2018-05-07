package sbs.model.proprog;

import java.sql.Timestamp;

public class Project {
	
	public static final int STATE_NEW = 1;
	public static final int STATE_IN_PROGRESS = 2;
	public static final int STATE_FINISHED = 3;
	
	public static final int TOOL_N_D = 1;
	public static final int TOOL_READY = 2;
	public static final int TOOL_NOT_NEEDED = 3;
	
	// main project info
	//+ "CAC.YCODPROG_0, " + "CAC.YDESCPROG_0, " + "CAC.YDTPROG_0, " + "CAC.YORAPROG_0, "
	private String projectNumber;
	private String projectDescription;
	private Timestamp projectCreationDate;
	 //"CAC.YDESCRIZIONE_0 "
	private String addidionalDescription;
	// client accept
	//+ "CAC.YACCCLI_0, " + "CAC.YDTACCCLI_0, " + "CAC.YORAACCCLI_0, "
	private boolean clientAccept;
	private Timestamp clientAcceptDate;
	// codification 
	//+ "CAC.YCODADR_0, " + "CAC.YDTCODADR_0, " + "CAC.YORACODADR_0, "
	private boolean codification;
	private Timestamp codificationDate;
	// drawing verified
	//+ "CAC.YDISVER_0, " + "CAC.YCODICE_0, " + "CAC.YDTUFFTEC_0, " + "CAC.YORAUFFTEC_0, "
	private boolean drawingsVerified;
	private String drawingsVerifiedCode;
	private Timestamp drawingsVerifiedDate;
	// sales order	
	//+ "CAC.YORDINE_0, " + "CAC.YPEZZIORD_0, " + "CAC.YDTORDINE_0, " + "CAC.YORAORDINE_0, "
	private boolean salesOrder;
	private int salesOrderQuantity;
	private Timestamp salesOrderDate;
	// purchase plan
	//+ "CAC.YPREVISTO_0, " + "CAC.YDTPREVISTO_0, " + "CAC.YORAPREVISTO_0, "
	private boolean purchasePlan;
	private Timestamp purchasePlanDate;
	// purchase
	//+ "CAC.YACQNEW_0, " + "CAC.YDTACQNEW_0, " + "CAC.YORAACQNEW_0, "
	private boolean newPurchase;
	private Timestamp newPurchaseDate;
	// tool drawing
	//+ "CAC.YATTREZ_0, " + "CAC.YDTATTREZ_0, " + "CAC.YORAATTREZ_0, "
	private int toolDrawing;
	private Timestamp toolDrawingDate;
	// tool creation
	//+ "CAC.YATTROK_0, " + "CAC.YDTATTROK_0, " + "CAC.YORAATTROK_0, "
	private boolean toolCreation;
	private Timestamp toolCreationDate;
	// technology
	//+ "CAC.YTECNOL_0, " + "CAC.YDTTECNOL_0, " + "CAC.YORATECNOL_0, "
	private boolean technology;
	private Timestamp technologyDate;
	// quality
	//+ "CAC.YQUALIT_0, " + "CAC.YDTQUALIT_0, " + "CAC.YORAQUALIT_0, "
	private boolean quality;
	private Timestamp qualityDate;
	// general project state
	//+ "CAC.YSTATOCA_0, "
	private int projectState;
	// ?
	
	
	public Project() {
		 
	}
	
	public int getProgressTotal(){
		// if finished
		if(projectState==Project.STATE_FINISHED){
			return 100;
		}
		// if pending
		int progress = 0;
		progress = (!this.clientAccept ? progress : (progress+10));
		progress = (!this.codification ? progress : (progress+10));
		progress = (!this.drawingsVerified ? progress : (progress+10));
		progress = (!this.salesOrder ? progress : (progress+10));
		progress = (!this.purchasePlan ? progress : (progress+10));
		progress = (!this.newPurchase ? progress : (progress+10));
		progress = (this.toolDrawing == Project.TOOL_N_D ? progress : (progress+10));
		// because not needed turns off need for tool creation:
		progress = (this.toolDrawing == Project.TOOL_NOT_NEEDED ? (progress+10) : progress);
		progress = (!this.toolCreation ? progress : (progress+10));
		progress = (!this.technology ? progress : (progress+10));
		progress = (!this.quality ? progress : (progress+10));
		
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

	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public Timestamp getProjectCreationDate() {
		return projectCreationDate;
	}

	public void setProjectCreationDate(Timestamp projectCreationDate) {
		this.projectCreationDate = projectCreationDate;
	}

	public String getAddidionalDescription() {
		return addidionalDescription;
	}

	public void setAddidionalDescription(String addidionalDescription) {
		this.addidionalDescription = addidionalDescription;
	}

	public boolean isClientAccept() {
		return clientAccept;
	}

	public void setClientAccept(boolean clientAccept) {
		this.clientAccept = clientAccept;
	}

	public Timestamp getClientAcceptDate() {
		return clientAcceptDate;
	}

	public void setClientAcceptDate(Timestamp clientAcceptDate) {
		this.clientAcceptDate = clientAcceptDate;
	}

	public boolean isCodification() {
		return codification;
	}

	public void setCodification(boolean codification) {
		this.codification = codification;
	}

	public Timestamp getCodificationDate() {
		return codificationDate;
	}

	public void setCodificationDate(Timestamp codificationDate) {
		this.codificationDate = codificationDate;
	}

	public boolean isDrawingsVerified() {
		return drawingsVerified;
	}

	public void setDrawingsVerified(boolean drawingsVerified) {
		this.drawingsVerified = drawingsVerified;
	}

	public String getDrawingsVerifiedCode() {
		return drawingsVerifiedCode;
	}

	public void setDrawingsVerifiedCode(String drawingsVerifiedCode) {
		this.drawingsVerifiedCode = drawingsVerifiedCode;
	}

	public Timestamp getDrawingsVerifiedDate() {
		return drawingsVerifiedDate;
	}

	public void setDrawingsVerifiedDate(Timestamp drawingsVerifiedDate) {
		this.drawingsVerifiedDate = drawingsVerifiedDate;
	}

	public boolean isSalesOrder() {
		return salesOrder;
	}

	public void setSalesOrder(boolean salesOrder) {
		this.salesOrder = salesOrder;
	}

	public int getSalesOrderQuantity() {
		return salesOrderQuantity;
	}

	public void setSalesOrderQuantity(int salesOrderQuantity) {
		this.salesOrderQuantity = salesOrderQuantity;
	}

	public Timestamp getSalesOrderDate() {
		return salesOrderDate;
	}

	public void setSalesOrderDate(Timestamp salesOrderDate) {
		this.salesOrderDate = salesOrderDate;
	}

	public boolean isPurchasePlan() {
		return purchasePlan;
	}

	public void setPurchasePlan(boolean purchasePlan) {
		this.purchasePlan = purchasePlan;
	}

	public Timestamp getPurchasePlanDate() {
		return purchasePlanDate;
	}

	public void setPurchasePlanDate(Timestamp purchasePlanDate) {
		this.purchasePlanDate = purchasePlanDate;
	}

	public boolean isNewPurchase() {
		return newPurchase;
	}

	public void setNewPurchase(boolean newPurchase) {
		this.newPurchase = newPurchase;
	}

	public Timestamp getNewPurchaseDate() {
		return newPurchaseDate;
	}

	public void setNewPurchaseDate(Timestamp newPurchaseDate) {
		this.newPurchaseDate = newPurchaseDate;
	}

	public int getToolDrawing() {
		return toolDrawing;
	}

	public void setToolDrawing(int toolDrawing) {
		this.toolDrawing = toolDrawing;
	}

	public Timestamp getToolDrawingDate() {
		return toolDrawingDate;
	}

	public void setToolDrawingDate(Timestamp toolDrawingDate) {
		this.toolDrawingDate = toolDrawingDate;
	}

	public boolean isToolCreation() {
		return toolCreation;
	}

	public void setToolCreation(boolean toolCreation) {
		this.toolCreation = toolCreation;
	}

	public Timestamp getToolCreationDate() {
		return toolCreationDate;
	}

	public void setToolCreationDate(Timestamp toolCreationDate) {
		this.toolCreationDate = toolCreationDate;
	}

	public boolean isTechnology() {
		return technology;
	}

	public void setTechnology(boolean technology) {
		this.technology = technology;
	}

	public Timestamp getTechnologyDate() {
		return technologyDate;
	}

	public void setTechnologyDate(Timestamp technologyDate) {
		this.technologyDate = technologyDate;
	}

	public boolean isQuality() {
		return quality;
	}

	public void setQuality(boolean quality) {
		this.quality = quality;
	}

	public Timestamp getQualityDate() {
		return qualityDate;
	}

	public void setQualityDate(Timestamp qualityDate) {
		this.qualityDate = qualityDate;
	}

	public int getProjectState() {
		return projectState;
	}

	public void setProjectState(int projectState) {
		this.projectState = projectState;
	}

	@Override
	public String toString() {
		return "Project [projectNumber=" + projectNumber + ", projectDescription=" + projectDescription
				+ ", projectCreationDate=" + projectCreationDate + ", addidionalDescription=" + addidionalDescription
				+ ", clientAccept=" + clientAccept + ", clientAcceptDate=" + clientAcceptDate + ", codification="
				+ codification + ", codificationDate=" + codificationDate + ", drawingsVerified=" + drawingsVerified
				+ ", drawingsVerifiedCode=" + drawingsVerifiedCode + ", drawingsVerifiedDate=" + drawingsVerifiedDate
				+ ", salesOrder=" + salesOrder + ", salesOrderQuantity=" + salesOrderQuantity + ", salesOrderDate="
				+ salesOrderDate + ", purchasePlan=" + purchasePlan + ", PurchasePlanDate=" + purchasePlanDate
				+ ", newPurchase=" + newPurchase + ", newPurchaseDate=" + newPurchaseDate + ", toolDrawing="
				+ toolDrawing + ", toolDrawingDate=" + toolDrawingDate + ", toolCreation=" + toolCreation
				+ ", toolCreationDate=" + toolCreationDate + ", technology=" + technology + ", technologyDate="
				+ technologyDate + ", quality=" + quality + ", qualityDate=" + qualityDate + ", projectState="
				+ projectState + "]";
	}
	
	
	
	
}

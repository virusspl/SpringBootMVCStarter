package sbs.model.downtimes;

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


@Entity
@Table(name = "downtimes")
public class Downtime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dt_downtime_id")
	private int id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dt_dtt_type", nullable = false)
	private DowntimeType type;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dt_dtrt_response_type", nullable = false)
	private DowntimeResponseType responseType;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dt_dtc_cause", nullable = false)
	private DowntimeCause cause;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "downtime")
	Set<DowntimeDetailsMaterial> materialDetails;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "downtime")
	Set<DowntimeDetailsFailure> failureDetails;
	
	@Column(name = "dt_machine_code", length = 15, nullable = false)
	private String machineCode;
	@Column(name = "dt_machine_name", length = 75, nullable = false)
	private String machineName;
	@Column(name = "dt_comment", length = 255, nullable = true)
	private String comment;
	@Column(name = "dt_opened", nullable = false )
	private boolean opened;
	
	@Column(name = "dt_init_date", nullable = false)
	private Timestamp startDate;	
	@Column(name = "dt_init_rcp_number", length = 25, nullable = false)
	private String initRcpNumber;
	@Column(name = "dt_init_first_name", length = 75, nullable = false)
	private String initFirstName;
	@Column(name = "dt_init_last_name", length = 75, nullable = false)
	private String initLastName;
	@Column(name = "dt_init_position", length = 150, nullable = false)
	private String initPosition;
	@Column(name = "dt_init_department", length = 150, nullable = false)
	private String initDepartment;
	
	@Column(name = "dt_end_date", nullable = true)
	private Timestamp endDate;	
	@Column(name = "dt_end_rcp_number", length = 25, nullable = true)
	private String endRcpNumber;
	@Column(name = "dt_end_first_name", length = 75, nullable = true)
	private String endFirstName;
	@Column(name = "dt_end_last_name", length = 75, nullable = true)
	private String endLastName;
	@Column(name = "dt_end_position", length = 150, nullable = true)
	private String endPosition;
	@Column(name = "dt_end_department", length = 150, nullable = true)
	private String endDepartment;
	@Column(name = "dt_end_comment", length = 255, nullable = true)
	private String endComment;

	@Column(name = "dt_response_date", nullable = true)
	private Timestamp responseDate;
	@Column(name = "dt_response_comment", length = 255, nullable = true)
	private String responseComment;
	
	public Downtime() {
	
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DowntimeType getType() {
		return type;
	}

	public void setType(DowntimeType type) {
		this.type = type;
	}

	public DowntimeCause getCause() {
		return cause;
	}

	public void setCause(DowntimeCause cause) {
		this.cause = cause;
	}

	public Set<DowntimeDetailsMaterial> getMaterialDetails() {
		return materialDetails;
	}

	public void setMaterialDetails(Set<DowntimeDetailsMaterial> materialDetails) {
		this.materialDetails = materialDetails;
	}

	public Set<DowntimeDetailsFailure> getFailureDetails() {
		return failureDetails;
	}

	public void setFailureDetails(Set<DowntimeDetailsFailure> failureDetails) {
		this.failureDetails = failureDetails;
	}

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getInitRcpNumber() {
		return initRcpNumber;
	}

	public void setInitRcpNumber(String initRcpNumber) {
		this.initRcpNumber = initRcpNumber;
	}

	public String getInitFirstName() {
		return initFirstName;
	}

	public void setInitFirstName(String initFirstName) {
		this.initFirstName = initFirstName;
	}

	public String getInitLastName() {
		return initLastName;
	}

	public void setInitLastName(String initLastName) {
		this.initLastName = initLastName;
	}

	public String getInitPosition() {
		return initPosition;
	}

	public void setInitPosition(String initPosition) {
		this.initPosition = initPosition;
	}

	public String getInitDepartment() {
		return initDepartment;
	}

	public void setInitDepartment(String initDepartment) {
		this.initDepartment = initDepartment;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getEndRcpNumber() {
		return endRcpNumber;
	}

	public void setEndRcpNumber(String endRcpNumber) {
		this.endRcpNumber = endRcpNumber;
	}

	public String getEndFirstName() {
		return endFirstName;
	}

	public void setEndFirstName(String endFirstName) {
		this.endFirstName = endFirstName;
	}

	public String getEndLastName() {
		return endLastName;
	}

	public void setEndLastName(String endLastName) {
		this.endLastName = endLastName;
	}

	public String getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(String endPosition) {
		this.endPosition = endPosition;
	}

	public String getEndDepartment() {
		return endDepartment;
	}

	public void setEndDepartment(String endDepartment) {
		this.endDepartment = endDepartment;
	}

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}
	
	public String getEndComment() {
		return endComment;
	}

	public void setEndComment(String endComment) {
		this.endComment = endComment;
	}

	@Override
	public String toString() {
		return "Downtime [id=" + id + ", type=" + type + ", cause=" + cause + ", materialDetails=" + materialDetails
				+ ", failureDetails=" + failureDetails + ", machineCode=" + machineCode + ", machineName=" + machineName
				+ ", comment=" + comment + ", opened=" + opened + ", startDate=" + startDate + ", initRcpNumber="
				+ initRcpNumber + ", initFirstName=" + initFirstName + ", initLastName=" + initLastName
				+ ", initPosition=" + initPosition + ", initDepartment=" + initDepartment + ", endDate=" + endDate
				+ ", endRcpNumber=" + endRcpNumber + ", endFirstName=" + endFirstName + ", endLastName=" + endLastName
				+ ", endPosition=" + endPosition + ", endDepartment=" + endDepartment + "]";
	}
	
	
	
	

}

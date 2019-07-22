package sbs.model.downtimes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "downtime_details_failure")
public class DowntimeDetailsFailure {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dtdf_details_id")
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dtdf_downtime_id", nullable = false)
	private Downtime downtime;
	
	@Column(name = "dtdf_failure_x3_number", length = 25, nullable = false)
	private String failureX3Number;
	
	@Column(name = "dtdf_asset_code", length = 15, nullable = false)
	private String assetCode;
	
	@Column(name = "dtdf_asset_name", length = 75, nullable = false)
	private String assetName;
	
	@Column(name = "dtdf_description", length = 255, nullable = false)
	private String description;
	
	@Column(name = "dtdf_type", length = 25, nullable = false)
	private String type;
	
	@Column(name = "dtdf_kind", length = 25, nullable = false)
	private String kind;
	
	public DowntimeDetailsFailure() {
	
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Downtime getDowntime() {
		return downtime;
	}

	public void setDowntime(Downtime downtime) {
		this.downtime = downtime;
	}

	public String getFailureX3Number() {
		return failureX3Number;
	}

	public void setFailureX3Number(String failureX3Number) {
		this.failureX3Number = failureX3Number;
	}

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	@Override
	public String toString() {
		return "DowntimeDetailsFailure [id=" + id + ", failureX3Number=" + failureX3Number + ", assetCode=" + assetCode
				+ ", assetName=" + assetName + ", description=" + description + ", type=" + type + ", kind=" + kind
				+ "]";
	}
	
	
	
}

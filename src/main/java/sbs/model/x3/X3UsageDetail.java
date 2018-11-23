package sbs.model.x3;

import java.sql.Timestamp;

public class X3UsageDetail {

	String productCode;
	int usage;
	Timestamp usageDate;
	
	public X3UsageDetail() {
		// TODO Auto-generated constructor stub
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public double getUsage() {
		return usage;
	}

	public void setUsage(int usage) {
		this.usage = usage;
	}

	public Timestamp getUsageDate() {
		return usageDate;
	}

	public void setUsageDate(Timestamp usageDate) {
		this.usageDate = usageDate;
	}

	@Override
	public String toString() {
		return "X3UsageDetail [productCode=" + productCode + ", usage=" + usage + ", usageDate=" + usageDate + "]";
	}
	
	
}

package sbs.model.x3;

import java.sql.Timestamp;

public class X3StandardCostEntry {

	private String code;
	private double totalCost;
	private double materialCost;
	private double machineCost;
	private double labourCost;
	private Timestamp date;
	
	public X3StandardCostEntry() {

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public double getMaterialCost() {
		return materialCost;
	}

	public void setMaterialCost(double materialCost) {
		this.materialCost = materialCost;
	}

	public double getMachineCost() {
		return machineCost;
	}

	public void setMachineCost(double machineCost) {
		this.machineCost = machineCost;
	}

	public double getLabourCost() {
		return labourCost;
	}

	public void setLabourCost(double labourCost) {
		this.labourCost = labourCost;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "X3StandardCostEntry [code=" + code + ", totalCost=" + totalCost + ", materialCost=" + materialCost
				+ ", machineCost=" + machineCost + ", labourCost=" + labourCost + ", date=" + date + "]";
	}
	
	
}
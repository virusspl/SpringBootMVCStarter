package sbs.controller.prodcomp;

import java.util.HashMap;
import java.util.Map;

public class PlanLine {
	
	private String order;
	private String code;
	private String description;
	private String date;
	private String clientName;
	private String country;
	private int quantity;
	private int maxProduction;
	private int shortageQuantity;
	
	private double lineValue;
	private double shortageValue;
	
	private Map<String, Double> requirements;
	private Map<String, Integer> shortage;
	
	public PlanLine() {
		requirements = new HashMap<>();
		shortage = new HashMap<>();
		shortageValue = 0;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Map<String, Double> getRequirements() {
		return requirements;
	}

	public void setRequirements(Map<String, Double> requirements, double multiplier) {
		this.requirements = new HashMap<>();
		for(Map.Entry<String, Double> entry: requirements.entrySet() ) {
			this.requirements.put(entry.getKey(), entry.getValue()*multiplier);
		}
	}

	public Map<String, Integer> getShortage() {
		return shortage;
	}

	public void setShortage(Map<String, Integer> shortage) {
		this.shortage = shortage;
	}
	
	public double getLineValue() {
		return lineValue;
	}

	public void setLineValue(double lineValue) {
		this.lineValue = lineValue;
	}

	public double getShortageValue() {
		return shortageValue;
	}

	public void setShortageValue(double shortageValue) {
		this.shortageValue = shortageValue;
	}
	

	public int getMaxProduction() {
		return maxProduction;
	}

	public void setMaxProduction(int maxProduction) {
		this.maxProduction = maxProduction;
		if(maxProduction < this.quantity) {
			this.shortageQuantity = this.quantity - maxProduction;
			this.shortageValue = this.shortageQuantity * this.getUnitPrice();
		}
		else {
			this.shortageQuantity = 0;
			this.shortageValue = 0;
		}
	}

	public int getShortageQuantity() {
		return shortageQuantity;
	}

	public void setShortageQuantity(int shortageQuantity) {
		this.shortageQuantity = shortageQuantity;
	}

	public void addRequirement(String code, double quantity) {
		if(requirements.containsKey(code)) {
			requirements.put(code, requirements.get(code)+Math.abs(quantity));
		}
		else {
			requirements.put(code, quantity);
		}
	}
	
	
	public void addShortage(String code, int quantity) {
		if(shortage.containsKey(code)) {
			shortage.put(code, shortage.get(code)+Math.abs(quantity));
		}
		else {
			shortage.put(code, quantity);
		}
	}

	@Override
	public String toString() {
		return "PlanLine [order=" + order + ", code=" + code + ", description=" + description + ", date=" + date
				+ ", clientName=" + clientName + ", country=" + country + ", quantity=" + quantity + "]" 
				+ System.getProperty("line.separator")
				+ " * requirements " + requirements
				+ System.getProperty("line.separator")
				+ " * shortage " + shortage;
	}
	
	public double getUnitPrice() {
		return this.lineValue / this.quantity;
	}
	
	
	
}

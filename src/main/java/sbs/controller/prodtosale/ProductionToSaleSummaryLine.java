package sbs.controller.prodtosale;

public class ProductionToSaleSummaryLine {

	private String group;
	private int quantityToday;
	private int quantityEndMonth;
	private int quantityNextMonth;
	private int quantityFurther;
	
	
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public int getQuantityToday() {
		return quantityToday;
	}
	public void setQuantityToday(int quantityToday) {
		this.quantityToday = quantityToday;
	}
	public int getQuantityEndMonth() {
		return quantityEndMonth;
	}
	public void setQuantityEndMonth(int quantityEndMonth) {
		this.quantityEndMonth = quantityEndMonth;
	}
	public int getQuantityNextMonth() {
		return quantityNextMonth;
	}
	public void setQuantityNextMonth(int quantityNextMonth) {
		this.quantityNextMonth = quantityNextMonth;
	}
	public int getQuantityFurther() {
		return quantityFurther;
	}
	public void setQuantityFurther(int quantityFurther) {
		this.quantityFurther = quantityFurther;
	}
	@Override
	public String toString() {
		return "ProductionToSaleSummaryLine [group=" + group + ", quantityToday=" + quantityToday
				+ ", quantityEndMonth=" + quantityEndMonth + ", quantityNextMonth=" + quantityNextMonth
				+ ", quantityFurther=" + quantityFurther + "]";
	}
	
	
}

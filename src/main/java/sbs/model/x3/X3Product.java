package sbs.model.x3;

public class X3Product {

	private String code;
	private String description;
	private String category;
	private String gr2;
	private boolean verifyStock;

	public X3Product() {
	
	}

	public X3Product(String code, String description, String category) {
		super();
		this.code = code;
		this.description = description;
		this.category = category;
	}
	
	public X3Product(String code, String description, String category, String gr2) {
		super();
		this.code = code;
		this.description = description;
		this.category = category;
		this.gr2 = gr2;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	

	public String getGr2() {
		return gr2;
	}

	public void setGr2(String gr2) {
		this.gr2 = gr2;
	}
	
	public boolean isVerifyStock() {
		return verifyStock;
	}

	public void setVerifyStock(boolean verifyStock) {
		this.verifyStock = verifyStock;
	}

	@Override
	public String toString() {
		return "X3Product [code=" + code + ", description=" + description + ", category=" + category + ", gr2=" + gr2
				+ "]";
	}


	
	
	
}

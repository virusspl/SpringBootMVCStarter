package sbs.controller.cebs;

public class MenuItem {

	String id;
	String text;
	Double price;
	
	public MenuItem() {

	}

	public MenuItem(String id, String text, Double price) {
		super();
		this.id = id;
		this.text = text;
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "MenuItem [id=" + id + ", text=" + text + ", price=" + price + "]";
	}
	
}

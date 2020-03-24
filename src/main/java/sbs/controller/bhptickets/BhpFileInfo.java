package sbs.controller.bhptickets;

public class BhpFileInfo {

	String name;
	String creationDate;
	
	public BhpFileInfo() {
	
	}
	
	public BhpFileInfo(String name, String creationDateAsString) {
		super();
		this.name = name;
		this.creationDate = creationDateAsString;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public String toString() {
		return "bhpFileInfo [name=" + name + ", creationDate=" + creationDate + "]";
	}
	
}

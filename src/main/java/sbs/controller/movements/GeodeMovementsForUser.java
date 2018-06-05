package sbs.controller.movements;

public class GeodeMovementsForUser {
	
	String code;
	String name;
	int loadCounter;
	int unloadCounter;
	int generalCounter;
	
	public GeodeMovementsForUser(){
		
	}
	
	

	public GeodeMovementsForUser(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}

	public void loadCounterIncrement(){
		loadCounter++;
		generalCounter++;
	}
	
	public void unloadCounterIncrement(){
		unloadCounter++;
		generalCounter++;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLoadCounter() {
		return loadCounter;
	}

	public void setLoadCounter(int loadCounter) {
		this.loadCounter = loadCounter;
	}

	public int getUnloadCounter() {
		return unloadCounter;
	}

	public void setUnloadCounter(int unloadCounter) {
		this.unloadCounter = unloadCounter;
	}

	public int getGeneralCounter() {
		return generalCounter;
	}

	public void setGeneralCounter(int generalCounter) {
		this.generalCounter = generalCounter;
	}

	@Override
	public String toString() {
		return "GeodeMovementsForUser [code=" + code + ", name=" + name + ", loadCounter=" + loadCounter
				+ ", unloadCounter=" + unloadCounter + ", generalCounter=" + generalCounter + "]";
	}
	
	
	
	
}

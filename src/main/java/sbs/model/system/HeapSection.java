package sbs.model.system;

import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.Map;

public class HeapSection {
	
	public static final int SECTION_INIT = 1;
	public static final int SECTION_MAX = 2;
	public static final int SECTION_USED = 3;
	public static final int SECTION_COMMITTED = 4;

	public static final int UNIT_MB = 1;
	public static final int UNIT_GB = 2;
	
	private String name;
	private MemoryUsage usage;
	
	
	public HeapSection() {
	
	}
	
	public void setSectionInfo(String name, MemoryUsage memoryUsage) {
		this.name = name;
		this.usage = memoryUsage;
	}
	
	public MemoryUsage getUsage() {
		return usage;
	}

	public void setUsage(MemoryUsage usage) {
		this.usage = usage;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	};
	
	public double getAmount(int amountType, int unit) {
		long amountL = 0;
		
		switch (amountType) {
		case SECTION_INIT:
			amountL = this.usage.getInit();
			break;
		case SECTION_MAX:
			amountL = this.usage.getMax();
			break;
		case SECTION_USED:
			amountL = this.usage.getUsed();
			break;
		case SECTION_COMMITTED:
			amountL = this.usage.getCommitted();
			break;
		default:
			return 0;
		}
		
		switch (unit) {
		case UNIT_MB:
			return amountL * 1.0 / 1024 / 1024;
		case UNIT_GB:
			return amountL * 1.0 / 1024 / 1024 / 1024;
		default:
			break;
		}
		
		return 0;
	}
	
	public Map<String, Object> getSectionSnap(){
		Map<String, Object> snap = new HashMap<>();
		snap.put("name", this.getName() );
		snap.put("init", this.getAmount(SECTION_INIT, UNIT_MB) );
		snap.put("max", this.getAmount(SECTION_MAX, UNIT_MB) );
		snap.put("used", this.getAmount(SECTION_USED, UNIT_MB));
		snap.put("usedProc", this.getAmount(SECTION_USED, UNIT_MB) * 100.0 / this.getAmount(SECTION_MAX, UNIT_MB));
		snap.put("committed", this.getAmount(SECTION_COMMITTED, UNIT_MB) );
		snap.put("committedProc", this.getAmount(SECTION_COMMITTED, UNIT_MB) * 100.0 / this.getAmount(SECTION_MAX, UNIT_MB));
		return snap;
	}
}

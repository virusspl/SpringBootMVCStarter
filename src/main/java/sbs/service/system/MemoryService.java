package sbs.service.system;

import sbs.model.system.HeapInfo;

public interface MemoryService {
	
	public HeapInfo getHeapInfo();
	public void debugListMemoryPoolBeans();
	
}

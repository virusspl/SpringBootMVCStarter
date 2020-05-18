package sbs.service.system;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;

import org.springframework.stereotype.Service;

import sbs.model.system.HeapInfo;

@Service
public class MemoryServiceImpl implements MemoryService {

	HeapInfo heapInfo;

	public MemoryServiceImpl() {
		heapInfo = new HeapInfo();
	}

	@Override
	public HeapInfo getHeapInfo() {
		MemoryMXBean mBean = ManagementFactory.getMemoryMXBean();
		heapInfo.setSectionInfoHeap("Heap", mBean.getHeapMemoryUsage());
		heapInfo.setSectionInfoNonHeap("Non-Heap", mBean.getNonHeapMemoryUsage());
		
		for (MemoryPoolMXBean mpBean : ManagementFactory.getMemoryPoolMXBeans()) {
			if (mpBean.getType() == MemoryType.HEAP) {
				switch (mpBean.getName()) {
				case "PS Eden Space":
					heapInfo.setSectionInfoEden(mpBean.getName(), mpBean.getUsage());
					break;
				case "PS Survivor Space":
					heapInfo.setSectionInfoSurvivor(mpBean.getName(), mpBean.getUsage());
					break;
				case "PS Old Gen":
					heapInfo.setSectionInfoOld(mpBean.getName(), mpBean.getUsage());
					break;
				default:
					break;
				}
			}
		}
		
		return heapInfo;
	}

}

package sbs.model.system;

import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HeapInfo {

	private HeapSection sectionHeap;
	private HeapSection sectionNonHeap;
	private HeapSection sectionEden;
	private HeapSection sectionSurvivor;
	private HeapSection sectionOld;
	private List<HeapSection> sections;
	
	
	public HeapInfo() {
		sectionHeap = new HeapSection();
		sectionNonHeap = new HeapSection();
		sectionEden = new HeapSection();
		sectionSurvivor = new HeapSection();
		sectionOld = new HeapSection();
		
		this.sections = new ArrayList<>();
		sections.add(sectionHeap);
		sections.add(sectionEden);
		sections.add(sectionSurvivor);
		sections.add(sectionOld);
		sections.add(sectionNonHeap);
	}

	public HeapSection getSectionHeap() {
		return sectionHeap;
	}

	public void setSectionHeap(HeapSection sectionHeap) {
		this.sectionHeap = sectionHeap;
	}
	
	
	public HeapSection getSectionNonHeap() {
		return sectionNonHeap;
	}

	public void setSectionNonHeap(HeapSection sectionNonHeap) {
		this.sectionNonHeap = sectionNonHeap;
	}

	public HeapSection getSectionEden() {
		return sectionEden;
	}

	public void setSectionEden(HeapSection sectionEden) {
		this.sectionEden = sectionEden;
	}

	public HeapSection getSectionSurvivor() {
		return sectionSurvivor;
	}

	public void setSectionSurvivor(HeapSection sectionSurvivor) {
		this.sectionSurvivor = sectionSurvivor;
	}

	public HeapSection getSectionOld() {
		return sectionOld;
	}

	public void setSectionOld(HeapSection sectionOld) {
		this.sectionOld = sectionOld;
	}

	public void setSectionInfoEden(String name, MemoryUsage memoryUsage) {
		this.sectionEden.setSectionInfo(name, memoryUsage);
	}

	public void setSectionInfoSurvivor(String name, MemoryUsage memoryUsage) {
		this.sectionSurvivor.setSectionInfo(name, memoryUsage);
	}

	public void setSectionInfoOld(String name, MemoryUsage memoryUsage) {
		this.sectionOld.setSectionInfo(name, memoryUsage);
	}
	
	public void setSectionInfoHeap(String name, MemoryUsage memoryUsage) {
		this.sectionHeap.setSectionInfo(name, memoryUsage);
	}
	public void setSectionInfoNonHeap(String name, MemoryUsage memoryUsage) {
		this.sectionNonHeap.setSectionInfo(name, memoryUsage);
	}

	public List<HeapSection> getSections() {
		return sections;
	}

	public void setSections(List<HeapSection> sections) {
		this.sections = sections;
	}
	
	public List<Map<String, Object>> getSectionsSnaps() {
		List<Map<String, Object>> snaps = new ArrayList<>();
		for(HeapSection section: this.getSections()) {
			snaps.add(section.getSectionSnap());
		}
		return snaps;
	}

}
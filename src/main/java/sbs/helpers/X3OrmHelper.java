package sbs.helpers;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import sbs.model.x3.X3UtrFault;
import sbs.model.x3.X3UtrFaultLine;
import sbs.model.x3.X3UtrMachine;
import sbs.model.x3.X3UtrWorker;

@Component
public class X3OrmHelper {

	public void fillUtrFaultsLinks() {

	}

	public void fillUtrFaultsLinks(Map<String, X3UtrFault> faults, List<X3UtrFaultLine> lines,
			Map<String, X3UtrWorker> workers, Map<String, X3UtrMachine> machines) {

		for (X3UtrFault fault : faults.values()) {
			// set fault machine
			fault.setMachine(machines.get(fault.getMachineCode()));
		}

		for (X3UtrFaultLine line : lines) {
			// add worker to line
			line.setWorker(workers.get(line.getUtrWorkerCode()));
			if (faults.get(line.getFaultNumber()) != null){
				// add line to fault
				faults.get(line.getFaultNumber()).getLines().add(line);
			}
		}
	}
}

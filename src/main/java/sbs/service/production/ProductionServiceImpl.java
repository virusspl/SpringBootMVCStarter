package sbs.service.production;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sbs.model.x3.X3RouteLine;
import sbs.service.x3.JdbcOracleX3Service;

@Service
public class ProductionServiceImpl implements ProductionService {
	
	private String[] assemblyCenters;
	private String[] weldingCenters;
	private String[] mechanicalCenters;
	private Map<String, String> workcenters;
	private Map<String, Map<Integer, X3RouteLine>> routesATW;
	private Map<String, Map<Integer, X3RouteLine>> routesWPS;
	@Autowired
	JdbcOracleX3Service x3Service;
	
	public ProductionServiceImpl() {
		assemblyCenters = new String[]{"08","09","10","11","12","13"};
		weldingCenters = new String[]{"15","16","18","19"};
		mechanicalCenters = new String[]{"01","02","03","04","06","07","14"};
		
	}

	private boolean isStringInArray(String str, String[] array){
		boolean result = false;
		for(String s: array){
			if(s.equals(str)){
				return true;
			}
		}
		return result;
	}

	@Override
	public String getMainDepartmentCodeByMachine(String machine, String company){
		
		if(company.equals("WPS")) {
			return DEPARTMENT_WPS;
		}
		
		String center ="";
		if(this.workcenters == null) {
			this.workcenters = x3Service.getWorkcenterNumbersMapByMachines(company);
		}
		center = workcenters.get(machine);

		
		if(isStringInArray(center, assemblyCenters)){
			return DEPARTMENT_ASSEMBLY;
		}
		else if(isStringInArray(center, weldingCenters)){
			return DEPARTMENT_WELDING;
		}
		else if(isStringInArray(center, mechanicalCenters)){
			return DEPARTMENT_MECHANICAL;
		}
		else{
			return DEPARTMENT_UNASSIGNED;
		}
	}
	
	@Override
	public X3RouteLine getLastRouteLineExcludingKAL(String productCode, String company){
		
		X3RouteLine result = null;
		Map<String, Map<Integer, X3RouteLine>> routes;
		
		if(company.contentEquals("ATW")) {
			if(routesATW == null) {
				this.routesATW = x3Service.getRoutesMap(company); // cache
			}
			routes = routesATW;
		}
		else {
			if(routesWPS == null) {
				this.routesWPS = x3Service.getRoutesMap(company); // cache
			}
			routes = routesWPS;			
		}
		
		
		Map<Integer, X3RouteLine> route = routes.get(productCode);
		if(route == null) {
			// return null if route doesn't exist for product code
			return null;
		}
		for(X3RouteLine entry: route.values()){
			if(entry.getMachine().startsWith("KAL")){
				if(result==null){
					result = entry;
				}
			}
			else {
				result = entry;
			}
		}
		return result;
		
	}




}

package sbs.service.production;

import sbs.model.x3.X3RouteLine;

public interface ProductionService{

	public static final String DEPARTMENT_ASSEMBLY = "department.assembly";
	public static final String DEPARTMENT_MECHANICAL = "department.mechanical";
	public static final String DEPARTMENT_WELDING = "department.welding";	
	public static final String DEPARTMENT_TOOLS= "department.tools";
	public static final String DEPARTMENT_UNASSIGNED= "general.unassigned";
	public static final String DEPARTMENT_WPS= "department.wps";
	
	public String getMainDepartmentCodeByMachine(String machine, String company);
	public X3RouteLine getLastRouteLineExcludingKAL(String productCode, String company);
	

}

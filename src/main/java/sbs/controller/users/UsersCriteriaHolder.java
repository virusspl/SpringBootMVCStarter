package sbs.controller.users;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UsersCriteriaHolder {
	private String findRange;
	private String sortOrder;

public String getFindRange() {
	return findRange;
}
public void setFindRange(String findRange) {
	this.findRange = findRange;
}
public String getSortOrder() {
	return sortOrder;
}
public void setSortOrder(String sortOrder) {
	this.sortOrder = sortOrder;
}


	
}

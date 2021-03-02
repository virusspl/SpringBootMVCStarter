package sbs.singleton;

import org.springframework.stereotype.Component;

import groovy.lang.Singleton;

@Component
@Singleton
public class LockSingleton {
	private boolean componentsLock;
	private String componentsLockUser;
	
	public LockSingleton() {
		this.componentsLock = false;
		this.componentsLockUser = "";
	}

	public boolean isComponentsLock() {
		return componentsLock;
	}

	public void setComponentsLock(boolean componentsLock) {
		this.componentsLock = componentsLock;
	}

	public String getComponentsLockUser() {
		return componentsLockUser;
	}

	public void setComponentsLockUser(String componentsLockUser) {
		this.componentsLockUser = componentsLockUser;
	}
	
	public void cancelComponentsLock() {
		this.componentsLock = false;
		this.componentsLockUser = "";
	}

	@Override
	public String toString() {
		return "LockSingleton [componentsLock=" + componentsLock + ", componentsLockUser=" + componentsLockUser + "]";
	}
	
	

		

	
}

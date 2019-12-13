package sbs.controller.auth;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import sbs.model.hr.HrUserInfo;

@Component
@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class HrUserInfoSessionHolder {

	private boolean set;
	private HrUserInfo info;

	public HrUserInfoSessionHolder() {
		this.set = false;
	}

	public HrUserInfoSessionHolder(HrUserInfo info) {
		this.set = true;
		this.info = info;
	}

	public boolean isSet() {
		return set;
	}

	public void setSet(boolean set) {
		this.set = set;
	}

	public HrUserInfo getInfo() {
		return info;
	}

	public void setInfo(HrUserInfo info) {
		this.set = true;
		this.info = info;
	}
	
	public void clear(){
		this.set = false;
		this.info = null;
	}

	@Override
	public String toString() {
		if (this.set) {
			return "HrUserInfoSessionHolder [set=" + set + ", info=" + info + "]";
		} else {
			return "HrUserInfoSessionHolder [set=" + set + ", not set]";
		}
	}

}

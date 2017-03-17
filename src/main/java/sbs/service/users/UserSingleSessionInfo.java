package sbs.service.users;

import java.sql.Timestamp;
import sbs.model.users.User;
import org.springframework.security.core.session.SessionInformation;

public class UserSingleSessionInfo {

	User user;
	String sessionId;
	Timestamp lastRequestDate;
	Boolean expired;
	
	public UserSingleSessionInfo() {

	}
	
	public UserSingleSessionInfo(User user, SessionInformation session) {
		this.user = user;
		this.sessionId = session.getSessionId();
		this.lastRequestDate = new Timestamp(session.getLastRequest().getTime());
		this.expired = session.isExpired();
	}

	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public Timestamp getLastRequestDate() {
		return lastRequestDate;
	}
	public void setLastRequestDate(Timestamp lastRequestDate) {
		this.lastRequestDate = lastRequestDate;
	}
	public Boolean getExpired() {
		return expired;
	}
	public void setExpired(Boolean expired) {
		this.expired = expired;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "UserSingleSessionInfo [user=" + user + ", sessionId=" + sessionId + ", lastRequestDate="
				+ lastRequestDate + ", expired=" + expired + "]";
	}

	
	
	
}

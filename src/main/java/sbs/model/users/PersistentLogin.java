package sbs.model.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*CREATE TABLE persistent_logins (
	    username varchar(64) not null,
	    series varchar(64) not null,
	    token varchar(64) not null,
	    last_used timestamp not null,
	    PRIMARY KEY (series)
	);*/

@Entity
@Table(name = "persistent_logins")
public class PersistentLogin {
	private String username;
	private String series;
	private String token;
    private java.util.Date lastUsed;

    public PersistentLogin() {

	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the series
	 */
	@Id
	public String getSeries() {
		return series;
	}

	/**
	 * @param series the series to set
	 */
	public void setSeries(String series) {
		this.series = series;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the lastUsed
	 */
	@Column(name = "last_used")
	public java.util.Date getLastUsed() {
		return lastUsed;
	}

	/**
	 * @param lastUsed the lastUsed to set
	 */
	public void setLastUsed(java.util.Date lastUsed) {
		this.lastUsed = lastUsed;
	}
    
    
    
    
    
}
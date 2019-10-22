package sbs.model.redmine;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RedmineUser {

	private int id;
	private String login;
	private String firstName;
	private String lastName;
	private String mail;
	private Timestamp createDate;
	private Timestamp updateDate;
	private Timestamp passChangeDate;
	private boolean admin;
	private List<RedmineProject> projects;
	private String copyString;
	
	public RedmineUser() {
		projects = new ArrayList<>();
	}

	public RedmineUser(int id, String login, String firstName, String lastName, boolean admin, String mail,
			Timestamp createDate, Timestamp updateDate, Timestamp passChangeDate) {
		super();
		this.id = id;
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
		this.admin = admin;
		this.mail = mail;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.passChangeDate = passChangeDate;
		this.copyString = "";
		this.projects = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}


	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public Timestamp getPassChangeDate() {
		return passChangeDate;
	}

	public void setPassChangeDate(Timestamp passChangeDate) {
		this.passChangeDate = passChangeDate;
	}
	
	public List<RedmineProject> getProjects() {
		return projects;
	}

	public void setProjects(List<RedmineProject> projects) {
		this.projects = projects;
	}

	public String getCopyString() {
		return copyString;
	}

	public void setCopyString(String copyString) {
		this.copyString = copyString;
	}

	@Override
	public String toString() {
		return "RedmineUser [id=" + id + ", login=" + login + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", mail=" + mail + ", createDate=" + createDate + ", updateDate=" + updateDate + ", passChangeDate="
				+ passChangeDate + ", admin=" + admin + ", projects=" + projects + "]";
	}

	public void createCopyString() {
		this.copyString = ""
				+ "login: " + this.login + "&#13;&#10;"
				+ "first name: " + this.firstName + "&#13;&#10;"
				+ "last name: " + this.lastName + "&#13;&#10;"
				+ "e-mail: " + this.mail + "&#13;&#10;"
				+ "project: ";
		
		for(RedmineProject project: this.projects) {
			this.copyString += project.getName() + "; ";
		}
		
		
	}
	
}

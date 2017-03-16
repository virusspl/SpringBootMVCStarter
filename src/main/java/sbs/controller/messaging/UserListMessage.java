package sbs.controller.messaging;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

public class UserListMessage implements Serializable{

	private static final long serialVersionUID = 6141348167516372321L;
	SortedSet<String> users;

	public UserListMessage() {
		users = new TreeSet<>();
	}

	public UserListMessage(SortedSet<String> users) {
		this.users = users;
	}

	/**
	 * @return the users
	 */
	public SortedSet<String> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(SortedSet<String> users) {
		this.users = users;
	}
	
	
	
}

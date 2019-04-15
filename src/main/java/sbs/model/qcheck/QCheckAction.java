package sbs.model.qcheck;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import sbs.model.users.User;

@Entity
@Table(name = "qcheck_actions")
public class QCheckAction {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qca_check_id", nullable = false)
	private QCheck check;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "qca_state_id", nullable = false)
	private QCheckState state;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "qca_user_id", nullable = true)
	private User user;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "qca_action_id")
	private int id;
	
	@Column(name = "qc_time", nullable = false)
	private Timestamp time;
	
	@Column(name = "qc_comment", length = 255, nullable = true)
	private String comment;
	
	
	public QCheckAction() {

	}


	public QCheck getCheck() {
		return check;
	}


	public void setCheck(QCheck check) {
		this.check = check;
	}


	public QCheckState getState() {
		return state;
	}


	public void setState(QCheckState state) {
		this.state = state;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Timestamp getTime() {
		return time;
	}


	public void setTime(Timestamp time) {
		this.time = time;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	@Override
	public String toString() {
		return "QCheckAction [check=" + check + ", state=" + state + ", user=" + user + ", id=" + id + ", time=" + time
				+ ", comment=" + comment + "]";
	}
	


}

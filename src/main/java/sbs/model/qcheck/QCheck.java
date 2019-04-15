package sbs.model.qcheck;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import sbs.model.users.User;

@Entity
@Table(name = "qcheck")
public class QCheck {
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "qc_creation_user_id", nullable = false)
	private User creator;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "qc_current_state_id", nullable = false)
	private QCheckState currentState;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "qc_current_user_id", nullable = true)
	private User currentUser;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "check")
	private Set<QCheckAction> actions;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "qc_check_id")
	private int id;
	
	@Column(name = "qc_creation_date", nullable = false)
	private Timestamp creationDate;
	
	@Column(name = "qc_product_code", length = 30, nullable = false)
	private String productCode;
	
	@Column(name = "qc_product_description", length = 150, nullable = false)
	private String productDescription;
	
	@Column(name = "qc_contents")
	@Type(type="text")
	private String contents;
	
	
	public QCheck() {
		actions = new HashSet<>();
	}


	public User getCreator() {
		return creator;
	}


	public void setCreator(User creator) {
		this.creator = creator;
	}

	
	public User getCurrentUser() {
		return currentUser;
	}


	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}


	public QCheckState getCurrentState() {
		return currentState;
	}


	public void setCurrentState(QCheckState currentState) {
		this.currentState = currentState;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Timestamp getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}


	public String getProductCode() {
		return productCode;
	}


	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}


	public String getProductDescription() {
		return productDescription;
	}


	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}


	public String getContents() {
		return contents;
	}


	public void setContents(String contents) {
		this.contents = contents;
	}
	

	public Set<QCheckAction> getActions() {
		return actions;
	}


	public void setActions(Set<QCheckAction> actions) {
		this.actions = actions;
	}


	@Override
	public String toString() {
		return "QCheck [creator=" + creator + ", currentState=" + currentState + ", actions=" + actions + ", id=" + id
				+ ", creationDate=" + creationDate + ", productCode=" + productCode + ", productDescription="
				+ productDescription + ", contents=" + contents + "]";
	}



}

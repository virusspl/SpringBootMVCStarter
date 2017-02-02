package sbs.controller.contact;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class ContactForm {
	@NotEmpty
	@Email
	private String email;
	@Size(min =  3, max = 50)
	private String name; 
	@Size(min = 20, max=500)
	private String content;
	
	public ContactForm(String email, String name, String content) {
		super();
		this.email = email;
		this.name = name;
		this.content = content;
	}

	public ContactForm() {
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	
	

}

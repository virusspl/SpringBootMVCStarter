package sbs.singleton;

import org.springframework.stereotype.Component;

import groovy.lang.Singleton;

@Component
@Singleton
public class LiveFeedSingleton {
	String message;
	
	public LiveFeedSingleton() {
		this.message = null;
	}

	public LiveFeedSingleton(String message) {
		super();
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return message;
	}

	
}

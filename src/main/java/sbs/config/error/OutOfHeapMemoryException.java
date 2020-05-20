package sbs.config.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class OutOfHeapMemoryException extends RuntimeException {
	private static final long serialVersionUID = -2190947596117377597L;
	
	public OutOfHeapMemoryException(String msg){
		super(msg);
	}
}

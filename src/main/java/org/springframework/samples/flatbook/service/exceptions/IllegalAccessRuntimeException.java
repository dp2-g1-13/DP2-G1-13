package org.springframework.samples.flatbook.service.exceptions;

public class IllegalAccessRuntimeException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public IllegalAccessRuntimeException(String errorMessage) {
        super(errorMessage);
    }
	
}

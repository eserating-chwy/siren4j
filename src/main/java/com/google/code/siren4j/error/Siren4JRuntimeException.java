package com.google.code.siren4j.error;

public class Siren4JRuntimeException extends RuntimeException implements Siren4JThrowable{

	private static final long serialVersionUID = 7764190460419228957L;

	public Siren4JRuntimeException() {
		super();
	}

	public Siren4JRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public Siren4JRuntimeException(String message) {
		super(message);
	}

	public Siren4JRuntimeException(Throwable cause) {
		super(cause);
	}
    
}

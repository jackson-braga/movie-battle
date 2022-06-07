package br.com.jackson.braga.moviebattle.exceptions;

public class NullModelException extends RuntimeException {

	public NullModelException() {
	}

	public NullModelException(String message) {
		super(message);
	}

	public NullModelException(Throwable cause) {
		super(cause);
	}

	public NullModelException(String message, Throwable cause) {
		super(message, cause);
	}

	public NullModelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

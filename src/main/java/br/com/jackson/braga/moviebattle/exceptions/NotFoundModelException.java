package br.com.jackson.braga.moviebattle.exceptions;

public class NotFoundModelException extends RuntimeException {

	public NotFoundModelException() {
	}

	public NotFoundModelException(String message) {
		super(message);
	}

	public NotFoundModelException(Throwable cause) {
		super(cause);
	}

	public NotFoundModelException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundModelException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

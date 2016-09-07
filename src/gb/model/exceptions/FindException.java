package gb.model.exceptions;

public class FindException extends Exception {

	private static final long serialVersionUID = -7208530847514158752L;

	public FindException() {
		super();
	}

	public FindException(String message) {
		super(message);
	}

	public FindException(Throwable throwable) {
		super(throwable);
	}

	public FindException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public FindException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

}

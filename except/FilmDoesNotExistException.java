package except;

public class FilmDoesNotExistException extends Exception {

	

	public FilmDoesNotExistException() {
		super();
	}

	public FilmDoesNotExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FilmDoesNotExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public FilmDoesNotExistException(String message) {
		super(message);
	}

	public FilmDoesNotExistException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2074326120712976377L;

}

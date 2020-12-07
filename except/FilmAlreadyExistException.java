package except;

public class FilmAlreadyExistException extends Exception {

	

	public FilmAlreadyExistException() {
		super();
	}

	public FilmAlreadyExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FilmAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public FilmAlreadyExistException(String message) {
		super(message);
	}

	public FilmAlreadyExistException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2074326120712976377L;

}

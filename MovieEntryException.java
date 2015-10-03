public class MovieEntryException extends Exception {
	private static final long serialVersionUID = 1L;

	public MovieEntryException(String message) {
		super(message);
	}
	
	public MovieEntryException(Throwable cause) {
		super(cause);
	}
}
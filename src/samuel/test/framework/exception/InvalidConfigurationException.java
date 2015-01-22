package samuel.test.framework.exception;

/**
 * The Exception to be thrown when configuration file is invalid
 */
public class InvalidConfigurationException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidConfigurationException(String string) {
		super(string);
	}
}

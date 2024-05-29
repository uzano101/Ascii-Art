package ascii_art;

/**
 * The IllegalRequest class represents an exception thrown when a request is illegal or does not conform
 * to expected format.
 */
public class IllegalRequest extends Exception {

    /**
     * Constructs a new IllegalRequest exception with the specified detail message.
     *
     * @param message The detail message.
     */
    public IllegalRequest(String message) {
        super(message);
    }
}

package ascii_art;

/**
 * The ExceedBound class represents an exception thrown when a value exceeds a specified bound.
 */
public class ExceedBound extends Exception {

    /**
     * Constructs a new ExceedBound exception with the specified detail message.
     *
     * @param message The detail message.
     */
    public ExceedBound(String message) {
        super(message);
    }
}

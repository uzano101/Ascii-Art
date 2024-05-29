package ascii_art;

import java.io.IOException;

/**
 * The FuncWithArgs interface represents a functional interface that defines a method with arguments.
 */
interface FuncWithArgs {
    /**
     * Executes a function with the provided request string.
     *
     * @param request The request string containing the arguments for the function.
     * @throws IOException      If an I/O error occurs while executing the function.
     * @throws ExceedBound      If a value exceeds a specified bound during function execution.
     * @throws IllegalRequest   If the request string is invalid or does not conform to expected format.
     */
    void run(String request) throws IOException, ExceedBound, IllegalRequest;
}

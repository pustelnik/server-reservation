package pl.san.jakub.tools.exceptions;

/**
 * Created by Jakub on 24.11.2015.
 */
public class PasswordIsNotChangedException extends Throwable {

    public PasswordIsNotChangedException(String message) {
        super(message);
    }

    public PasswordIsNotChangedException(String message, Throwable cause) {
        super(message, cause);
    }
}

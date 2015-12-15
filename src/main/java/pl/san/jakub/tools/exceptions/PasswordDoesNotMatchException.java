package pl.san.jakub.tools.exceptions;

/**
 * Created by Jakub on 29.11.2015.
 */
public class PasswordDoesNotMatchException extends Exception{

    public PasswordDoesNotMatchException(String message) {
        super(message);
    }

    public PasswordDoesNotMatchException(Throwable cause) {
        super(cause);
    }
}

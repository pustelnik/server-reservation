package pl.san.jakub.tools.exceptions;

/**
 * Created by Jakub on 04.06.2016.
 */
public class UserAlreadyExistException extends GeneralServerException {

    public UserAlreadyExistException() {}

    public UserAlreadyExistException(Throwable cause) {
        super(cause);
    }

    public UserAlreadyExistException(String message) {
        super(message);
    }

    public UserAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}

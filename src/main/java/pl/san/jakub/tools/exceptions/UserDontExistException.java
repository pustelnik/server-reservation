package pl.san.jakub.tools.exceptions;

/**
 * Created by Jakub on 04.06.2016.
 */
public class UserDontExistException extends GeneralServerException {

    public UserDontExistException() {}

    public UserDontExistException(Throwable cause) {
        super(cause);
    }

    public UserDontExistException(String message) {
        super(message);
    }

    public UserDontExistException(String message, Throwable cause) {
        super(message, cause);
    }
}

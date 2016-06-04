package pl.san.jakub.tools.exceptions;

/**
 * Created by Jakub on 19.12.2015.
 */
public class GeneralServerException extends Exception {

    public GeneralServerException() {

    }

    public GeneralServerException(Throwable cause) {
        super(cause);
    }

    public GeneralServerException(String message) {
        super(message);
    }

    public GeneralServerException(String message, Throwable cause) {
        super(message, cause);
    }
}

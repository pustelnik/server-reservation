package pl.san.jakub.tools.exceptions;

/**
 * Created by Jakub on 10.12.2015.
 */
public class ServerCreationException extends Exception {

    public ServerCreationException(String message) {
        super(message);
    }

    public ServerCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerCreationException(Throwable cause) {
        super(cause);
    }
}

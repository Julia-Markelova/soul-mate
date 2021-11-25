package ifmo.soulmate.demo.exceptions;


public class AuthException extends Exception {
    public AuthException(String msg) {
        super(msg);
    }

    public AuthException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

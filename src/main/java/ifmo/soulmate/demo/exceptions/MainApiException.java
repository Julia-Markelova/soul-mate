package ifmo.soulmate.demo.exceptions;

import org.springframework.http.HttpStatus;

public class MainApiException extends Exception {
    String message;
    HttpStatus status;

    public MainApiException(String msg) {
        super(msg);
    }

    public MainApiException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public MainApiException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
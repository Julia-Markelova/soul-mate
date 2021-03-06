package ifmo.soulmate.demo.exceptions;


import org.springframework.http.HttpStatus;

public class AuthException extends MainApiException {
    String message;
    HttpStatus status;

    public AuthException(String message, HttpStatus status) {
        super(message, status);
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

package ifmo.soulmate.demo.exceptions;


import org.springframework.http.HttpStatus;

public class NonExistingEntityException extends MainApiException {
    String message;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    HttpStatus status = HttpStatus.NOT_FOUND;

    public NonExistingEntityException(String msg) {
        super(msg, HttpStatus.NOT_FOUND);
        this.message = msg;
    }

    public NonExistingEntityException(String msg, Throwable cause) {
        super(msg, cause);
    }


    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

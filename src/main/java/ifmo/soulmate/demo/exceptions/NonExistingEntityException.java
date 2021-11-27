package ifmo.soulmate.demo.exceptions;


public class NonExistingEntityException extends Exception {
    String message;

    public NonExistingEntityException(String msg) {
        super(msg);
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

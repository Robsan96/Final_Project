package ittalents_final_project.ninegag.Utils.Exceptions;

public class NotAnEmailOrInvalidUsernameException extends Exception {

    String message;

    public NotAnEmailOrInvalidUsernameException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

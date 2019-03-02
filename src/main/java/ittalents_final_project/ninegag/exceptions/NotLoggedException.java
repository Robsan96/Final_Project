package ittalents_final_project.ninegag.exceptions;

public class NotLoggedException extends Exception {

    public NotLoggedException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "you are not logged in.";
    }
}

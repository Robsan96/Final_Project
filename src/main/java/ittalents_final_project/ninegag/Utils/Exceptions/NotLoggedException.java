package ittalents_final_project.ninegag.Utils.Exceptions;

public class NotLoggedException extends Exception {

    public NotLoggedException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "You are not logged in.";
    }
}

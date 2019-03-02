package ittalents_final_project.ninegag.utilities.exceptions;

public class NotAdminException extends Exception {

    public NotAdminException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Not an admin";
    }
}

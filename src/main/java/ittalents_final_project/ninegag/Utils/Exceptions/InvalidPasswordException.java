package ittalents_final_project.ninegag.utilities.exceptions;

public class InvalidPasswordException extends Exception {

    @Override
    public String getMessage() {
        return "Password must be 8 characters or more, have at least one upper and lower case character and have at least 1 special character and digit and no spaces.";
    }
}

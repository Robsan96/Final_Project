package ittalents_final_project.ninegag.Utils.Exceptions;

public class NotAnEmailException extends Exception {

    @Override
    public String getMessage() {
        return "This is not a real email.";
    }
}

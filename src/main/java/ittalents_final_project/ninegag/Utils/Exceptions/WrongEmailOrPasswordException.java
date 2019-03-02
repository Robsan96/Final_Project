package ittalents_final_project.ninegag.Utils.Exceptions;

public class WrongEmailOrPasswordException extends Exception {

    @Override
    public String getMessage() {
        return "Wrong username or password.";
    }
}

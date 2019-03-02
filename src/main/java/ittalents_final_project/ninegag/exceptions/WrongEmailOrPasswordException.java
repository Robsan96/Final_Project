package ittalents_final_project.ninegag.exceptions;

public class WrongEmailOrPasswordException extends Exception {

    @Override
    public String getMessage() {
        return "Wrong username or password.";
    }
}


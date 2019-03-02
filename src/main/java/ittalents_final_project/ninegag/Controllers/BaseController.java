package ittalents_final_project.ninegag.Controllers;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import ittalents_final_project.ninegag.Models.POJO.User;
import ittalents_final_project.ninegag.exceptions.NotAdminException;
import ittalents_final_project.ninegag.exceptions.NotLoggedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public abstract class BaseController {

    @ExceptionHandler({NullPointerException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMsg handleEmptyResult(Exception e) {
        return new ErrorMsg(e.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
    }

    @ExceptionHandler({NotLoggedException.class, NotAdminException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMsg handleLoggingOrAdminStatus(Exception e) {
        return new ErrorMsg(e.getMessage(), HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now());
    }

    protected static final String LOGGED = "logged";

    protected void validateLogged(HttpSession session) throws NotLoggedException {
        if (session.getAttribute(LOGGED) == null) {
            throw new NotLoggedException("Not Logged");
        }
    }

    protected void validateAdmin(HttpSession session) throws NotLoggedException, NotAdminException {
        if (session.getAttribute(LOGGED)==null){
            throw new NotLoggedException("Not Logged");
        }else {
            User user =(User)session.getAttribute(LOGGED);
            if(!user.isAdmin_privileges()){
                throw new NotAdminException("Not Admin");
            }
        }
    }

    protected void logInUser(User user, HttpSession session) {
        session.setMaxInactiveInterval(-1);
        session.setAttribute(LOGGED, user);
    }

    protected void logOutUser(HttpSession session){
        session.setAttribute(LOGGED,null);
    }

    protected boolean validatePassword(String password){
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(password);
        return matcher.matches();
    }
}

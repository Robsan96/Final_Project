package ittalents_final_project.ninegag.Controllers;

import ittalents_final_project.ninegag.Models.POJO.User;
import ittalents_final_project.ninegag.Utils.Exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@RestController
public abstract class BaseController {

    @ExceptionHandler({EmptyParameterException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMsg handleEmptyParamExeption(Exception e) {
        return new ErrorMsg(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
    }

    @ExceptionHandler({NullPointerException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMsg handleEmptyResult(Exception e) {
        return new ErrorMsg(e.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
    }

    @ExceptionHandler({NotLoggedException.class, NotAdminException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMsg hadleLoggingOrAdminStatus(Exception e) {
        return new ErrorMsg(e.getMessage(), HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now());
    }

//   @ExceptionHandler({SQLException.class})
//   @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorMsg handleMySQL(Exception e) {
//        return new ErrorMsg("Error in the DataBase query", HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
//   }

    @ExceptionHandler({AlreadyExistsException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMsg handleAlreadyExistsException(Exception e) {
        return new ErrorMsg(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
    }


    protected static final String LOGGED = "logged";

    protected void validateLogged(HttpSession session) throws NotLoggedException {
        if (session.getAttribute(LOGGED) == null) {
            throw new NotLoggedException("Not Logged");
        }
    }

    protected void validateAdmin(HttpSession session) throws NotLoggedException, NotAdminException {
        if (session.getAttribute(LOGGED) == null) {
            throw new NotLoggedException("Not Logged");
        } else {
            User user = (User) session.getAttribute(LOGGED);
            if (!user.isAdmin_privileges()) {
                throw new NotAdminException("Not Admin");
            }
        }
    }

    protected void logInUser(User user, HttpSession session) {
        session.setMaxInactiveInterval(-1);
        session.setAttribute(LOGGED, user);
    }

    protected void logOutUser(HttpSession session) {
        session.setAttribute(LOGGED, null);
    }

}
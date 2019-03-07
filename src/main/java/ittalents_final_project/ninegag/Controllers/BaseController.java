package ittalents_final_project.ninegag.Controllers;

import ittalents_final_project.ninegag.Models.POJO.User;
import ittalents_final_project.ninegag.Utils.Exceptions.*;
import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public abstract class BaseController {

static Logger log = Logger.getLogger(BaseController.class.getName());

    @ExceptionHandler({MessagingException.class, NullPointerException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMsg handleEmptyResult(Exception e) {
        log.error(e.getMessage());
        return new ErrorMsg(e.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
    }

    @ExceptionHandler({EmptyParameterException.class, BadParamException.class, AlreadyExistsException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMsg handleEmptyParamExeption(Exception e) {
        log.error(e.getMessage());
        return new ErrorMsg(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
    }

    @ExceptionHandler({NotLoggedException.class, NotAdminException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMsg handleLoggingOrAdminStatus(Exception e) {
        log.error(e.getMessage());
        return new ErrorMsg(e.getMessage(), HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now());
    }


//
//    @ExceptionHandler({SQLException.class})
//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorMsg handleMySQL(Exception e) {
//        log.error(e.getMessage());
//        return new ErrorMsg("Error in the DataBase query", HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
//    }

<<<<<<< HEAD
    @ExceptionHandler({AlreadyExistsException.class,EmptyParameterException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMsg handleAlreadyExistsExceptionAndEmptyParamException(Exception e) {
        log.error(e.getMessage());
        return new ErrorMsg(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
    }

=======
>>>>>>> 1bb67467e71cd74b327115786cb871dbc6fda967
    @ExceptionHandler({WrongEmailOrPasswordException.class, EmptyResultDataAccessException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMsg handleLoggingWrongEmailOrPassword(Exception e) {
        log.error(e.getMessage());
        return new ErrorMsg("Wrong email or password.", HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now());
    }

    @ExceptionHandler({InvalidPasswordException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMsg handleInvalidPassword(Exception e) {
        log.error(e.getMessage());
        return new ErrorMsg(e.getMessage(), HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now());
    }

    protected static final String LOGGED = "logged";

    protected void validateLogged(HttpSession session) throws NotLoggedException {
        if (session.getAttribute(LOGGED) == null) {
            throw new NotLoggedException("Not Logged");
        }
    }

    protected boolean validateAdmin(HttpSession session) throws NotLoggedException{
        if (session.getAttribute(LOGGED) == null) {
            throw new NotLoggedException("Not Logged");
        } else {
            User user = (User) session.getAttribute(LOGGED);
            if (user.isAdmin_privileges()) {
                return true;
            } else return false;
        }
    }

    protected boolean validatePassword(String password){
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=*()-_<>])(?=\\S+$).{8,}$";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(password);
        return matcher.matches();
    }
}

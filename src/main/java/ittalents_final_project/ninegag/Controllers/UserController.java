package ittalents_final_project.ninegag.Controllers;

import ittalents_final_project.ninegag.Models.DAO.UserDAOImplem;
import ittalents_final_project.ninegag.Models.POJO.User;
import ittalents_final_project.ninegag.utilities.PasswordUtils;
import ittalents_final_project.ninegag.utilities.exceptions.InvalidPasswordException;
import ittalents_final_project.ninegag.utilities.exceptions.NotAdminException;
import ittalents_final_project.ninegag.utilities.exceptions.NotLoggedException;
import ittalents_final_project.ninegag.utilities.exceptions.WrongEmailOrPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

@RestController
public class UserController extends BaseController {

    public static final String LOGGED = "logged";
    @Autowired
    UserDAOImplem dao;

    @PostMapping(value = "/login")
    public void login(@RequestBody User user, HttpSession session) throws WrongEmailOrPasswordException, EmptyResultDataAccessException {
        boolean passwordMatch = PasswordUtils.verifyUserPassword(user.getPassword(), dao.findUserByEmail(user.getEmail()).getPassword(), dao.findUserByEmail(user.getEmail()).getSalt());
        if (passwordMatch) {
            session.setAttribute(LOGGED, dao.findUserByEmail(user.getEmail()));
            session.setMaxInactiveInterval(-1);
        } else {
            throw new WrongEmailOrPasswordException();
        }
    }

    @PostMapping(value = "/logout")
    public void logOut(HttpSession session) throws NotLoggedException{
        validateLogged(session);
        session.setAttribute(LOGGED, null);
    }

    @PostMapping(value = "/register")
    public void saveUser(@RequestBody User user, HttpSession session) throws MessagingException,InvalidPasswordException {
        if (validatePassword(user.getPassword())) {
            String salt = PasswordUtils.getSalt(30);
            String securedPassword = PasswordUtils.generateSecurePassword(user.getPassword(), salt);
            user.setPassword(securedPassword);
            user.setSalt(salt);
            dao.addUser(user);
            EmailController email = new EmailController();
            email.setEmail(user.getEmail());
            email.setName(user.getUsername());
            Thread thread = new Thread(email);
            thread.start();
            session.setAttribute(LOGGED, user);
        } else {
            throw new InvalidPasswordException();
        }
    }

    @PostMapping(value="/updateUser")
    public void updateUser(@RequestBody User user, HttpSession session) throws NotLoggedException{
        validateLogged(session);
        User transferUser = (User) session.getAttribute(LOGGED);
        user.setUser_ID(transferUser.getUser_ID());
        dao.updateUserByID(user);
    }

    @PostMapping(value="/updateUserAdmin")
    public void updateUserAdmin(@RequestBody User user, HttpSession session) throws NotLoggedException, NotAdminException{
        validateAdmin(session);
        dao.updateUserByEmail(user);
    }

    @DeleteMapping(value= "/deleteUser")
    public void deleteUser(HttpSession session) throws NotLoggedException{
        validateLogged(session);
        User user = (User) session.getAttribute(LOGGED);
        dao.deleteUserByID(user.getUser_ID());
    }

    @DeleteMapping(value= "/deleteUserAdmin")
    public void deleteUserAdmin(@RequestBody User user, HttpSession session) throws NotLoggedException, NotAdminException{
        validateAdmin(session);
        dao.deleteUserByEmail(user.getEmail());
    }
}

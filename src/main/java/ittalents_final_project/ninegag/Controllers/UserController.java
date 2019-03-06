package ittalents_final_project.ninegag.Controllers;

import ittalents_final_project.ninegag.Models.DAO.UserDAOImplem;
import ittalents_final_project.ninegag.Models.POJO.User;
import ittalents_final_project.ninegag.Utils.Exceptions.InvalidPasswordException;
import ittalents_final_project.ninegag.Utils.Exceptions.NotAdminException;
import ittalents_final_project.ninegag.Utils.Exceptions.NotLoggedException;
import ittalents_final_project.ninegag.Utils.Exceptions.WrongEmailOrPasswordException;
import ittalents_final_project.ninegag.Utils.PasswordUtils;
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
    public void login(@RequestBody User user, HttpSession session){
        try {
            boolean passwordMatch = PasswordUtils.verifyUserPassword(user.getPassword(), dao.findUserByEmail(user.getEmail()).getPassword(), dao.findUserByEmail(user.getEmail()).getSalt());
            if (passwordMatch) {
                session.setAttribute(LOGGED, dao.findUserByEmail(user.getEmail()));
                session.setMaxInactiveInterval(-1);
            } else {
                try {
                    throw new WrongEmailOrPasswordException();
                } catch (WrongEmailOrPasswordException e) {
                    e.getMessage();
                }
            }
        }
        catch (EmptyResultDataAccessException e){
            e.getMessage();
        }
    }

    @PostMapping(value = "/logout")
    public void logOut(HttpSession session){
        try {
            validateLogged(session);
            session.setAttribute(LOGGED, null);
        }
        catch (NotLoggedException e){
            e.getMessage();
        }
    }

    @PostMapping(value = "/register")
    public void saveUser(@RequestBody User user, HttpSession session) throws MessagingException {
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
            try {
                throw new InvalidPasswordException();
            } catch (InvalidPasswordException e) {
                e.getMessage();
            }
        }
    }

    @PostMapping(value="/updateUser")
    public void updateUser(@RequestBody User user, HttpSession session){
        try {
            validateLogged(session);
            User transferUser = (User) session.getAttribute(LOGGED);
            user.setUser_ID(transferUser.getUser_ID());
            dao.updateUserByID(user);
        }
        catch (NotLoggedException e){
            e.getMessage();
        }
    }

    @PostMapping(value="/updateUserAdmin")
    public void updateUserAdmin(@RequestBody User user, HttpSession session){
        try {
            validateAdmin(session);
            dao.updateUserByEmail(user);
        } catch (NotLoggedException e) {
            e.printStackTrace();
        } catch (NotAdminException e) {
            e.printStackTrace();
        }
    }

    @PostMapping(value= "/deleteUser")
    public void deleteUser(HttpSession session){
        try {
            validateLogged(session);
            User user = (User) session.getAttribute(LOGGED);
            dao.deleteUserByID(user.getUser_ID());
        }
        catch (NotLoggedException e){
            e.getMessage();
        }
    }

    @PostMapping(value= "/deleteUserAdmin")
    public void deleteUserAdmin(@RequestBody User user, HttpSession session){
        try {
            validateAdmin(session);
            dao.deleteUserByEmail(user.getEmail());
        } catch (NotLoggedException e) {
            e.printStackTrace();
        } catch (NotAdminException e) {
            e.printStackTrace();
        }
    }
}

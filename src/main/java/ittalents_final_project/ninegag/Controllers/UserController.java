package ittalents_final_project.ninegag.Controllers;

import ittalents_final_project.ninegag.Models.DAO.UserDAOImplem;
import ittalents_final_project.ninegag.Models.POJO.User;
import ittalents_final_project.ninegag.Utils.Exceptions.InvalidPasswordException;
import ittalents_final_project.ninegag.Utils.Exceptions.NotLoggedException;
import ittalents_final_project.ninegag.Utils.Exceptions.WrongEmailOrPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class UserController extends BaseController {

    public static final String LOGGED = "logged";
    @Autowired
    UserDAOImplem dao;

    @PostMapping(value = "/login")
    public void login(@RequestBody User user, HttpSession session){
        boolean passwordMatch = PasswordUtils.verifyUserPassword(user.getPassword(), dao.findUserByEmail(user.getEmail()).getPassword(), dao.findUserByEmail(user.getEmail()).getSalt());
        if(passwordMatch) {
            session.setAttribute(LOGGED, dao.findUserByEmail(user.getEmail()));
            session.setMaxInactiveInterval(-1);
        }
        else{
            try {
                throw new WrongEmailOrPasswordException();
            } catch (WrongEmailOrPasswordException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @PostMapping(value = "/logout")
    public void logOut(HttpSession session){
        try {
            validateLogged(session);
            session.setAttribute(LOGGED, null);
        }
        catch (NotLoggedException e){
            System.out.println(e.getMessage());
        }
    }

    @PostMapping(value = "/register")
    public void saveUser(@RequestBody User user, HttpSession session){
        try {
            validateLogged(session);
            if (validatePassword(user.getPassword())) {
                String salt = PasswordUtils.getSalt(30);
                String securedPassword = PasswordUtils.generateSecurePassword(user.getPassword(), salt);
                user.setPassword(securedPassword);
                user.setSalt(salt);
                dao.addUser(user);
                session.setAttribute(LOGGED, user);
            } else {
                try {
                    throw new InvalidPasswordException();
                } catch (InvalidPasswordException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        catch (NotLoggedException e){
            System.out.println(e.getMessage());
        }
        //TODO pra6tane na email

    }

    @PostMapping(value="/updateUser")
    public void updateUser(@RequestBody User user, HttpSession session){
        try {
            validateLogged(session);
            User transferUser = (User) session.getAttribute(LOGGED);
            user.setUser_ID(transferUser.getUser_ID());
            dao.updateUser(user);
        }
        catch (NotLoggedException e){
            System.out.println(e.getMessage());
        }
    }

    @PostMapping(value= "/deleteUser")
    public void deleteUser(HttpSession session){
        try {
            validateLogged(session);
            User user = (User) session.getAttribute(LOGGED);
            dao.deleteUser(user.getUser_ID());
        }
        catch (NotLoggedException e){
            System.out.println(e.getMessage());
        }
    }


}

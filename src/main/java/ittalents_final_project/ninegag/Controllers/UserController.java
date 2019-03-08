package ittalents_final_project.ninegag.Controllers;

import ittalents_final_project.ninegag.Models.DAO.UserDAOImplem;
import ittalents_final_project.ninegag.Models.DTO.UserCommentsDTO;
import ittalents_final_project.ninegag.Models.DTO.UserPostsDTO;
import ittalents_final_project.ninegag.Models.DTO.UserUpvotesDTO;
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

    public void logOut(HttpSession session) throws NotLoggedException {
        validateLogged(session);
        session.setAttribute(LOGGED, null);
    }

    @PostMapping(value = "/register")
    public void saveUser(@RequestBody User user, HttpSession session) throws MessagingException, InvalidPasswordException {
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

    @PostMapping(value = "/updateUser")
    public void updateUser(@RequestBody User user, HttpSession session) throws NotLoggedException {
        validateLogged(session);
        User transferUser = (User) session.getAttribute(LOGGED);
        user.setUser_ID(transferUser.getUser_ID());
        dao.updateUserByID(user);
    }

    @PostMapping(value = "/updateUserAdmin")
    public void updateUserAdmin(@RequestBody User user, HttpSession session)
            throws NotLoggedException, NotAdminException {
        if (validateAdmin(session)) {
            dao.updateUserByEmail(user);
        } else {
            throw new NotAdminException("You dont have acces to that option");
        }
    }

    @DeleteMapping(value = "/deleteUser")
    public void deleteUser(HttpSession session) throws NotLoggedException {
        validateLogged(session);
        User user = (User) session.getAttribute(LOGGED);
        dao.deleteUserByID(user.getUser_ID());
        }

    @DeleteMapping(value = "/deleteUserAdmin")
    public void deleteUserAdmin(@RequestBody User user, HttpSession session) throws NotLoggedException, NotAdminException {
        if (validateAdmin(session)) {
            dao.deleteUserByEmail(user.getEmail());
        } else {
            throw new NotAdminException("You dont have acces to that option");
        }
    }

    @GetMapping(value = "/user/posts/{id}")
    public UserPostsDTO getUserPosts(@PathVariable(value = "id") int user_ID) {
        UserPostsDTO userPosts = dao.getUserPosts(user_ID);
        if (userPosts == null) {
            throw new NullPointerException("No posts for this user.");
        }
        return userPosts;
    }

    @GetMapping(value = "/user/comments/{id}")
    public UserCommentsDTO getUserCommentedPosts(@PathVariable(value = "id") int user_ID) {
        UserCommentsDTO userCommentedPosts = dao.getUserCommentedPosts(user_ID);
        if (userCommentedPosts == null) {
            throw new NullPointerException("No posts for this user.");
        }
        return userCommentedPosts;
    }

    @GetMapping(value = "/user/upvotes/{id}")
    public UserUpvotesDTO getUserUpvotedPosts(@PathVariable(value = "id") int user_ID) {
        UserUpvotesDTO userUpvotedPosts = dao.getUserUpvotedPosts(user_ID);
        if (userUpvotedPosts == null) {
            throw new NullPointerException("No posts for this user.");
        }
        return userUpvotedPosts;
    }
}

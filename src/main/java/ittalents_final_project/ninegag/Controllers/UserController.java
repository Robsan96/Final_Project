package ittalents_final_project.ninegag.Controllers;

import ittalents_final_project.ninegag.Models.DAO.CountryDAO;
import ittalents_final_project.ninegag.Models.DAO.GenderDAO;
import ittalents_final_project.ninegag.Models.DAO.UserDAOImplem;
import ittalents_final_project.ninegag.Models.DTO.UserCommentsDTO;
import ittalents_final_project.ninegag.Models.DTO.UserDTO;
import ittalents_final_project.ninegag.Models.DTO.UserPostsDTO;
import ittalents_final_project.ninegag.Models.DTO.UserUpvotesDTO;
import ittalents_final_project.ninegag.Models.POJO.User;
import ittalents_final_project.ninegag.Utils.Exceptions.*;
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
@RequestMapping(value = "/user")
public class UserController extends BaseController {

    public static final String LOGGED = "logged";
    @Autowired
    UserDAOImplem dao;

    @Autowired
    GenderDAO genderDAO;

    @Autowired
    CountryDAO countryDAO;

    @PostMapping(value = "/login")
    public UserDTO login(@RequestBody User user, HttpSession session) throws WrongEmailOrPasswordException, EmptyResultDataAccessException {
        boolean passwordMatch = PasswordUtils.verifyUserPassword(user.getPassword(), dao.findUserByEmail(user.getEmail()).getPassword(), dao.findUserByEmail(user.getEmail()).getSalt());
        if (passwordMatch) {
            session.setAttribute(LOGGED, dao.findUserByEmail(user.getEmail()));
            session.setMaxInactiveInterval(-1);
            return dao.getUserInfo(user.getEmail());
        } else {
            throw new WrongEmailOrPasswordException();
        }
    }

    @PostMapping(value = "/logout")

    public String logOut(HttpSession session) throws NotLoggedException {
        validateLogged(session);
        session.setAttribute(LOGGED, null);
        return "You have logged out.";
    }

    @PostMapping(value = "/register")
    public UserDTO saveUser(@RequestBody User user, HttpSession session) throws MessagingException, InvalidPasswordException, AlreadyExistsException, EmptyResultDataAccessException {
        try {
            if (dao.findUserByEmail(user.getEmail()) != null || dao.findUserByUsername(user.getUsername()) != null) {
                throw new AlreadyExistsException("You have already registered with this email or username.");
            }
        }
        catch (EmptyResultDataAccessException e){
            if (validatePassword(user.getPassword())) {
                setPassword(user);
                dao.addUser(user);
                EmailController email = new EmailController();
                email.setEmail(user.getEmail());
                email.setName(user.getUsername());
                Thread thread = new Thread(email);
                thread.start();
                session.setAttribute(LOGGED, user);
                return dao.getUserInfo(user.getEmail());
            }
            else {
                throw new InvalidPasswordException();
            }
        }
        return null;
    }

    @PostMapping(value = "/updateUser")
    public UserDTO updateUser(@RequestBody User user, HttpSession session) throws NotLoggedException {
        validateLogged(session);
        genderDAO.getById(user.getGender_ID());
        countryDAO.getById(user.getCountry_ID());
        User transferUser = (User) session.getAttribute(LOGGED);
        user.setUser_ID(transferUser.getUser_ID());
        dao.updateUserByID(user);
        return dao.getUserInfo(transferUser.getEmail());
    }

    @PostMapping(value = "/updateUserAdmin")
    public UserDTO updateUserAdmin(@RequestBody User user, HttpSession session)
            throws NotLoggedException, NotAdminException {
        if (validateAdmin(session)) {
            genderDAO.getById(user.getGender_ID());
            countryDAO.getById(user.getCountry_ID());
            User toBeDeleted = dao.findUserByEmail(user.getEmail());
            dao.updateUserByEmail(user);
            return dao.getUserInfo(toBeDeleted.getEmail());
        } else {
            throw new NotAdminException("You dont have access to that option.");
        }
    }

    @DeleteMapping(value = "/deleteUser")
    public int deleteUser (HttpSession session) throws NotLoggedException {
        validateLogged(session);
        User user = (User) session.getAttribute(LOGGED);
        int id = user.getUser_ID();
        dao.deleteUserByID(user.getUser_ID());
        return id;
    }


    @DeleteMapping(value = "/deleteUserAdmin")
    public int deleteUserAdmin(@RequestBody User user, HttpSession session) throws NotLoggedException, NotAdminException {
        if (validateAdmin(session)) {
            User toBeDeleted = dao.findUserByID(user.getUser_ID());
            int id = toBeDeleted.getUser_ID();
            dao.deleteUserByID(id);
            return id;
        } else {
            throw new NotAdminException("You dont have access to that option.");
        }
    }

    @PostMapping(value = "/changePassword")
    public String changePassword(@RequestBody User user, HttpSession session)throws NotLoggedException{
        validateLogged(session);
        if(validatePassword(user.getNewPassword())){
            user.setPassword(user.getNewPassword());
            setPassword(user);
            user.setNewPassword(null);
            return "Password changed successfully.";
        }
        return null;
    }

    public void setPassword(User user){
        String salt = PasswordUtils.getSalt(30);
        String securedPassword = PasswordUtils.generateSecurePassword(user.getPassword(), salt);
        user.setPassword(securedPassword);
        user.setSalt(salt);
    }

    @GetMapping(value = "/posts/{id}")
    public UserPostsDTO getUserPosts(@PathVariable(value = "id") int user_ID) {
        UserPostsDTO userPosts = dao.getUserPosts(user_ID);
        if (userPosts == null) {
            throw new NullPointerException("No posts for this user.");
        }
        return userPosts;
    }

    @GetMapping(value = "/comments/{id}")
    public UserCommentsDTO getUserCommentedPosts(@PathVariable(value = "id") int user_ID) {
        UserCommentsDTO userCommentedPosts = dao.getUserCommentedPosts(user_ID);
        if (userCommentedPosts == null) {
            throw new NullPointerException("No posts for this user.");
        }
        return userCommentedPosts;
    }

    @GetMapping(value = "/upvotes/{id}")
    public UserUpvotesDTO getUserUpvotedPosts(@PathVariable(value = "id") int user_ID) {
        UserUpvotesDTO userUpvotedPosts = dao.getUserUpvotedPosts(user_ID);
        if (userUpvotedPosts == null) {
            throw new NullPointerException("No posts for this user.");
        }
        return userUpvotedPosts;
    }
}

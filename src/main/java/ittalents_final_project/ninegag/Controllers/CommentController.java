package ittalents_final_project.ninegag.Controllers;

import ittalents_final_project.ninegag.Models.DAO.CommentDAO;
import ittalents_final_project.ninegag.Models.POJO.Comment;
import ittalents_final_project.ninegag.Models.POJO.User;
import ittalents_final_project.ninegag.Utils.Exceptions.EmptyParameterException;
import ittalents_final_project.ninegag.Utils.Exceptions.NotAdminException;
import ittalents_final_project.ninegag.Utils.Exceptions.NotLoggedException;
import ittalents_final_project.ninegag.Utils.Exceptions.PermitionDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/comments")
public class CommentController extends BaseController {

    @Autowired
    CommentDAO daoC;

    @PostMapping(value = "/add")
    public String addComment(@RequestBody Comment comment, HttpSession session) throws NotLoggedException, EmptyParameterException, NotAdminException, PermitionDeniedException {
        validateLogged(session);
        User user = (User) session.getAttribute(LOGGED);
        comment.setProfile((int) user.getUser_ID());
        if (comment.getContent() == null) {
            throw new EmptyParameterException("Comment field 'content' is empty(null) or wrong written!");
        }
        if (comment.getPost() == 0) {
            throw new EmptyParameterException("Comment field 'post' is empty or wrong written!");
        }

        if (comment.getProfile() == 0) {
            throw new EmptyParameterException("Comment field 'profile' is empty or wrong written");
        }
        System.out.println(comment.getProfile() + "---" + user.getUser_ID());

        if (daoC.addComment(comment) == 1) {
            return "Comment added successfully";
        } else {
            return "Comment was not added for some reason,pls try again!";
        }
    }

    @PostMapping(value = "/votes")
    public String voteComment(@RequestParam("commentId") int commentId,
                              @RequestParam("vote") boolean vote,
                              HttpSession session) throws EmptyParameterException, NotLoggedException, PermitionDeniedException, NotAdminException {
        validateLogged(session);
        User user = (User) session.getAttribute(LOGGED);
        if (user.getUser_ID() == 0) {
            throw new EmptyParameterException("Comment field 'content' is empty(null) or wrong written!");
        }
        if (commentId == 0) {
            throw new EmptyParameterException("Comment field 'content' is empty(null) or wrong written!");
        }
        Comment comment = daoC.getById(commentId);
        if (comment == null) {
            throw new NullPointerException("There is no comment with that id !");
        }
        if (daoC.voteComment(2, commentId, vote) == 1) {
            return "Voted";
        } else {
            return "Vote failed , pls try again";
        }
    }

    //TODO debate if needed !
    @GetMapping(value = "/votes/{commentId}")
    public int getAllVotesByComment(@PathVariable("commentId") int commentId) {
        if (daoC.getById(commentId) == null) {
            throw new NullPointerException("Comment with that id does not exist");
        } else {
            return daoC.getAllVotes(commentId);
        }
    }

    @DeleteMapping(value = "/{commentId}")
    public String deleteComment(@PathVariable(value = "commentId") int commentId, HttpSession session) throws NotLoggedException, PermitionDeniedException, NotAdminException {
        validateLogged(session);
        User user = (User) session.getAttribute(LOGGED);
        Comment comment = daoC.getById(commentId);
        if (user.getUser_ID() != comment.getProfile() && !validateAdmin(session)) {
            throw new PermitionDeniedException("You dont have acces to that option");
        }
        if (comment == null) {
            throw new NullPointerException("Comment with that id does not exist !");
        }
        if (daoC.deleteComment(comment) != 0) {
            return "Comment deleted!";
        } else {
            return "Error comment was not deleted";
        }
    }
}

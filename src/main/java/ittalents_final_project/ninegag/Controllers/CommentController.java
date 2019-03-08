package ittalents_final_project.ninegag.Controllers;

import ittalents_final_project.ninegag.Models.DAO.CommentDAO;
import ittalents_final_project.ninegag.Models.DAO.PostDAO;
import ittalents_final_project.ninegag.Models.DTO.ResponseCommentDTO;
import ittalents_final_project.ninegag.Models.POJO.Comment;
import ittalents_final_project.ninegag.Models.POJO.Post;
import ittalents_final_project.ninegag.Models.POJO.User;
import ittalents_final_project.ninegag.Utils.Exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(value = "/comments")
public class CommentController extends BaseController {

    @Autowired
    PostDAO daoP;
    @Autowired
    CommentDAO daoC;

    @PostMapping(value = "/add")
    public String addComment(@RequestBody Comment comment, HttpSession session)
            throws NotLoggedException, EmptyParameterException, BadParamException {
        validateLogged(session);
        User user = (User) session.getAttribute(LOGGED);
        comment.setProfile(user.getUser_ID());
        if (comment.getContent() == null || comment.getContent().length() == 0) {
            throw new EmptyParameterException("Comment field 'content' is empty(null) or wrong written!");
        }
        Post post = daoP.getPostById(comment.getPost());
        if (post == null) {
            throw new BadParamException("Post with that id does not exist and cant be commented");
        }
        if (daoC.addComment(comment) > 0) {
            return "Comment was added successfully";
        } else {
            return "Something went wrong with adding reply , pls try again";
        }
    }

    @PostMapping(value = "/add/reply")
    public String addCommentReply(@RequestBody Comment comment, HttpSession session) throws NotLoggedException, EmptyParameterException, BadParamException {
        validateLogged(session);
        User user = (User) session.getAttribute(LOGGED);
        comment.setProfile(user.getUser_ID());
        Comment mainComment = daoC.getById(comment.getReply());
        if (comment.getContent() == null || comment.getContent().length() == 0) {
            throw new EmptyParameterException("Comment field 'content' is empty(null) or wrong written!");
        }
        if (mainComment == null) {
            throw new BadParamException("cant reply on non-existing comment!");
        } else {
            comment.setPost(mainComment.getPost());
        }
        if (daoC.addReply(comment) > 0) {
            return "Reply was added successfully";
        } else {
            return "Something went wrong with adding reply , pls try again";
        }
    }

    @PostMapping(value = "/votes")
    public String voteComment(@RequestParam("commentId") int commentId,
                              @RequestParam("vote") boolean vote,
                              HttpSession session) throws EmptyParameterException, NotLoggedException {
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

    @GetMapping(value = "/fresh/{postId}")
    public List<ResponseCommentDTO> getAllCommentsSortByDate(@PathVariable("postId") int postId) {
        List<ResponseCommentDTO> comments = daoC.getAllFreshByPostDTO(postId);
        if (comments == null) {
            throw new NullPointerException("There are no comments for this post!");
        }
        return comments;
    }

    @GetMapping(value = "/replies/{commentId}")
    public List<ResponseCommentDTO> getAllRepliesOfComment(@PathVariable("commentId") int commentId) throws BadParamException {
        Comment comment = daoC.getById(commentId);
        if (comment == null) {
            throw new BadParamException("There are no comment with that id !");
        }
        List<ResponseCommentDTO> comments = daoC.getAllByCommentDTO(commentId);
        if (comments == null) {
            throw new NullPointerException("There are no replies for that comment (" + commentId + ")");
        } else {
            return comments;
        }
    }

    @PutMapping(value = "/update")
    public String uppdateCommentContent(@RequestBody Comment comment, HttpSession session)
            throws NotLoggedException, PermitionDeniedException {
        validateLogged(session);
        User user = (User) session.getAttribute(LOGGED);
        if (daoC.getById(comment.getId()) == null) {
            throw new NullPointerException("Comment with that id does not exist");
        } else {
            if (comment.getProfile() == user.getUser_ID() || validateAdmin(session)) {
                if (daoC.uppdateComment(comment) > 0) {
                    return "Comment with ID " + comment.getId() + " updated!";
                } else {
                    return "Comment wasnt update for some reason pls contact support!";
                }
            } else {
                throw new PermitionDeniedException("You dont have acces to that option!");
            }
        }
    }

    @DeleteMapping(value = "/{commentId}")
    public String deleteComment(@PathVariable(value = "commentId") int commentId, HttpSession session)
            throws NotLoggedException, PermitionDeniedException, BadParamException {
        validateLogged(session);
        User user = (User) session.getAttribute(LOGGED);
        Comment comment = daoC.getById(commentId);
        if (comment == null) {
            throw new BadParamException("Comment with that id does not exist !");
        }
        if (user.getUser_ID() != comment.getProfile() && !validateAdmin(session)) {
            throw new PermitionDeniedException("You don't have access to that option");
        }
        return "Comment deleted with id " + daoC.deleteComment(comment);
    }
}
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
        comment.setContent(comment.getContent().trim());
        if (comment.getContent() == null || comment.getContent().isEmpty()) {
            throw new EmptyParameterException("Comment field 'content' cant be null or empty !");
        }
        Post post = daoP.getPostById(comment.getPost());
        if (post == null) {
            throw new BadParamException("Post with that id does not exist and cant be commented");
        }
        return "Comment was added successfully with ID -> " + daoC.addComment(comment);
    }

    @PostMapping(value = "/add/reply")
    public String addCommentReply(@RequestBody Comment comment, HttpSession session) throws NotLoggedException, EmptyParameterException, BadParamException {
        validateLogged(session);
        User user = (User) session.getAttribute(LOGGED);
        comment.setProfile(user.getUser_ID());
        Comment mainComment = daoC.getById(comment.getReply());
        comment.setContent(comment.getContent().trim());
        if (comment.getContent() == null || comment.getContent().isEmpty()) {
            throw new EmptyParameterException("Comment field 'content' cannot be null or empty!");
        }
        if (mainComment == null) {
            throw new BadParamException("cant reply on non-existing comment!");
        } else {
            comment.setPost(mainComment.getPost());
        }
        return "Reply was added successfully with id -> " + daoC.addReply(comment);
    }

    @PostMapping(value = "/votes")
    public String voteComment(@RequestParam("commentId") int commentId,
                              @RequestParam("vote") boolean vote,
                              HttpSession session) throws NotLoggedException, BadParamException {
        validateLogged(session);
        User user = (User) session.getAttribute(LOGGED);
        Comment comment = daoC.getById(commentId);
        if (comment == null) {
            throw new BadParamException("There is no comment with that id to like !");
        }
        if (daoC.voteComment(user.getUser_ID(), commentId, vote) == 1) {
            return "Voted";
        } else {
            return "Vote failed , pls try again";
        }
    }

    @GetMapping(value = "/{postId}/fresh")
    public List<ResponseCommentDTO> getAllCommentsByPostFresh(@PathVariable("postId") int postId) {
        List<ResponseCommentDTO> comments = daoC.getAllFreshByPostDTO(postId);
        return comments;
    }

    @GetMapping(value = "/{commentId}")
    public ResponseCommentDTO getCommentByid(@PathVariable("commentId") int commentId) {
        ResponseCommentDTO comment = daoC.getById(commentId);
        if (comment == null) {
            throw new NullPointerException("There are no comment with that Id !");
        } else {
            return comment;
        }
    }

    @GetMapping(value = "/replies/{commentId}")
    public List<ResponseCommentDTO> getAllRepliesOfComment(@PathVariable("commentId") int commentId)
            throws BadParamException {
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
            throws NotLoggedException, PermitionDeniedException, EmptyParameterException {
        validateLogged(session);
        User user = (User) session.getAttribute(LOGGED);
        comment.setProfile(user.getUser_ID());
        comment.setContent(comment.getContent().trim());
        if (daoC.getById(comment.getId()) == null) {
            throw new NullPointerException("Comment with that id does not exist");
        }
        if (comment.getContent() == null || comment.getContent().isEmpty()) {
            throw new EmptyParameterException("Comment field 'content' cant be null or empty !");
        }
        if (comment.getProfile() == user.getUser_ID() || validateAdmin(session)) {
            if (daoC.uppdateComment(comment) > 0) {
                return "Comment with ID " + comment.getId() + " updated!";
            } else {
                return "Comment wasn't updated for some reason pls try again later or contact support!";
            }
        } else {
            throw new PermitionDeniedException("You don't have access to that option!");
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
        try {
            return "Comment deleted with id " + daoC.deleteComment(comment);
        } catch (Exception e) {
            log.error(e.getMessage());
            return "Comment can not be deleted right now , pls try again later or contact support";
        }
    }
}
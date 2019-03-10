package ittalents_final_project.ninegag.Controllers;

import ittalents_final_project.ninegag.Models.DAO.PostDAO;
import ittalents_final_project.ninegag.Models.DAO.SectionDAO;
import ittalents_final_project.ninegag.Models.DAO.TagDAO;
import ittalents_final_project.ninegag.Models.DTO.ResponsePostDTO;
import ittalents_final_project.ninegag.Models.POJO.Post;
import ittalents_final_project.ninegag.Models.POJO.Section;
import ittalents_final_project.ninegag.Models.POJO.User;
import ittalents_final_project.ninegag.Utils.Exceptions.BadParamException;
import ittalents_final_project.ninegag.Utils.Exceptions.NotLoggedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(value = "/posts")
public class PostController extends BaseController {

    @Autowired
    PostDAO dao;
    @Autowired
    SectionDAO daoS;
    @Autowired
    TagDAO daoT;

    @GetMapping(value = "/id/{postId}")
    public ResponsePostDTO getPostById(@PathVariable(value = "postId") int id) throws BadParamException {
        ResponsePostDTO post = dao.getBPostDTO(id, true);
        if (post == null) {
            throw new BadParamException("Post with that id does not exist!");
        }
        return post;
    }

    @GetMapping(value = "/{sectionId}")
    public List<ResponsePostDTO> getAllPostsBySection(@PathVariable(value = "sectionId") int sectionId,
                                                      @RequestParam(value = "ordered") String ordered) throws BadParamException {
        if (daoS.getById(sectionId) == null) {
            throw new BadParamException("Invalid section id !");
        }
        List<ResponsePostDTO> posts = dao.getAllPostsBySection(sectionId, ordered);
        return posts;
    }

    @GetMapping(value = "/tag/{tagId}")
    public List<ResponsePostDTO> getAllPostsByTag(@PathVariable(value = "tagId") int tagId,
                                                  @RequestParam(value = "ordered") String ordered)
            throws BadParamException {
        if (daoT.getById(tagId) == null) {
            throw new BadParamException("Invalid tag id !");
        }
        List<ResponsePostDTO> posts = dao.getAllPostsByTag(tagId, ordered);
        return posts;
    }

    @PutMapping(value = "/votes")
    public String votePost(@RequestParam("postId") int postId,
                           @RequestParam("vote") boolean vote,
                           HttpSession session) throws NotLoggedException, BadParamException {
        validateLogged(session);
        User user = (User) session.getAttribute(LOGGED);
        if (dao.getPostById(postId) == null) {
            throw new BadParamException("Post with that Id does not exist!");
        }
        if (dao.votePost(user.getUser_ID(), postId, vote) > 0) {
            return "Voted";
        } else {
            return "Vote failed , pls try again";
        }
    }

    @DeleteMapping(value = "/{postId}")
    public String deletePost(@PathVariable(value = "postId") int postId, HttpSession session) throws BadParamException, NotLoggedException {
        validateLogged(session);
        User user = (User) session.getAttribute(LOGGED);
        Post post = dao.getPostById(postId);
        if (post == null) {
            throw new BadParamException("Post with that Id does not exist!");
        }
        if (user.getUser_ID() == post.getProfileID() || validateAdmin(session)) {
            try {
                return "Deleted post with Id: " + dao.removePost(post);
            } catch (Exception e) {
                return "Sorry , you cant delete thist post for some reason, pls try again later or contact support";
            }
        } else {
            return "You dont have acces to that option";
        }
    }
}

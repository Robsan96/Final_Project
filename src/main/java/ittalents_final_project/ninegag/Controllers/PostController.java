package ittalents_final_project.ninegag.Controllers;

import ittalents_final_project.ninegag.Models.DAO.PostDAO;
import ittalents_final_project.ninegag.Models.DTO.ResponsePostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/posts")
public class PostController extends BaseController {

    @Autowired
    PostDAO dao;

    @GetMapping(value = "/{id}")
    public ResponsePostDTO getPostById(@PathVariable(value = "id") int id) {
        ResponsePostDTO post=dao.getBPostDTO(id);
        if(post==null){
            throw new NullPointerException("Post with that id does not exist!");
        }
        return post;
    }
}

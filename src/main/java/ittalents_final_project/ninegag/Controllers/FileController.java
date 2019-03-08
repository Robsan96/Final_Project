package ittalents_final_project.ninegag.Controllers;

import ittalents_final_project.ninegag.Models.DAO.PostDAO;
import ittalents_final_project.ninegag.Models.DAO.TagDAO;
import ittalents_final_project.ninegag.Models.DAO.UserDAOImplem;
import ittalents_final_project.ninegag.Models.DTO.RequestPostDTO;
import ittalents_final_project.ninegag.Models.POJO.Tag;
import ittalents_final_project.ninegag.Models.POJO.User;
import ittalents_final_project.ninegag.Utils.Exceptions.EmptyParameterException;
import ittalents_final_project.ninegag.Utils.Exceptions.NotLoggedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

@RestController
public class FileController extends BaseController {

    @Autowired
    UserDAOImplem daoU;
    @Autowired
    PostDAO daoP;
    @Autowired
    RequestPostDTO dtoC;
    @Autowired
    TagDAO daoT;

    private static final String FILE_PATH = "C:\\Users\\NN\\Desktop\\Pictures\\";
    public static final String FILE_NAME = System.currentTimeMillis() + ".jpg";

    @PostMapping(value = "/images/profiles")
    public void upploadImageToProfile(@RequestParam(value = "URL") String url, HttpSession session)
            throws NotLoggedException {
        validateLogged(session);
        if (url.isEmpty() || url == null) {
            throw new NullPointerException("URL is not valid or empty!");
        }
        String base64 = url;
        User user = (User) session.getAttribute(LOGGED);
        byte[] bytes = Base64.getDecoder().decode(base64);
        String fileName = user.getEmail() + FILE_NAME;
        File newFile = new File(FILE_PATH + fileName);
        try (FileOutputStream fos = new FileOutputStream(newFile)) {
            fos.write(bytes);
            user.setAvatar(newFile.getName());

            daoU.updateUserByID(user);

        } catch (IOException e) {
            System.out.println("Error in uploading avatar!");
        }
    }

    @PostMapping(value = "posts/add")
    @Transactional
    public String upploadImageToPost(@RequestBody RequestPostDTO postDTO, HttpSession session)
            throws NotLoggedException, IOException {
        validateLogged(session);
        User user = (User) session.getAttribute(LOGGED);
        postDTO.setProfileID(user.getUser_ID());
        if (postDTO.getContentURL().isEmpty() || postDTO.getContentURL() == null) {
            throw new NullPointerException("URL is not valid or empty!");
        }
        if (postDTO.getTitle().isEmpty() || postDTO.getTitle() == null) {
            throw new NullPointerException("Title of the post cannot be empty ");
        }
        postDTO.setContentURL(CreateImage(postDTO));
        int postId = daoP.addPost(postDTO);
        if (postDTO.getTags().size() > 0 || postDTO.getTags() != null) {
            daoT.setTags(postId, postDTO.getTags());
        }
        return "Post was created with ID -> " + postId;
    }

    private String CreateImage(RequestPostDTO requestPostDTO) throws IOException {
        String base64 = requestPostDTO.getContentURL();
        byte[] bytes = Base64.getDecoder().decode(base64);
        String fileName = requestPostDTO.getProfileID() + FILE_NAME;
        File newFile = new File(FILE_PATH + fileName);
        try (FileOutputStream fos = new FileOutputStream(newFile)) {
            fos.write(bytes);
            requestPostDTO.setContentURL(newFile.getName());
            return requestPostDTO.getContentURL();
        } catch (IOException e) {
            throw new IOException("Error in uploading post image!");
        }
    }

    @GetMapping(value = "/images/{name}", produces = "image/png")
    public byte[] downloadImage(@PathVariable(value = "name") String imageName) throws Exception {
        File newFile = new File(FILE_PATH + imageName);
        if (!newFile.exists()) {
            throw new EmptyParameterException("Oop file with that name does not exist !");
        }
        try {
            byte[] bytes = Files.readAllBytes(newFile.toPath());
            return bytes;
        } catch (IOException e) {
            throw new Exception("Something rly got out of hand ! this msg shoud not be displayed ever !");
        }
    }
}


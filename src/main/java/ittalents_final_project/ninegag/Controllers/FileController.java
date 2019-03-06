package ittalents_final_project.ninegag.Controllers;

import ittalents_final_project.ninegag.Models.DAO.PostDAO;
import ittalents_final_project.ninegag.Models.DAO.UserDAO;
import ittalents_final_project.ninegag.Models.POJO.User;
import ittalents_final_project.ninegag.Utils.Exceptions.EmptyParameterException;
import ittalents_final_project.ninegag.Utils.Exceptions.NotLoggedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

@RestController
@RequestMapping(value = "/images")
public class FileController extends BaseController {

    @Autowired
    UserDAO daoU;
    @Autowired
    PostDAO daoP;

    private static final String FILE_PATH = "C:\\Users\\NN\\Desktop\\Pictures\\";
    public static final String FILE_NAME = System.currentTimeMillis() + ".jpg";

    @PostMapping(value = "/profiles")
    public void upploadImageToProfile(@RequestParam(value = "URL") String url, HttpSession session) throws NotLoggedException {
        validateLogged(session);
        if (url.isEmpty() || url == null) {
            throw new NullPointerException("URL is not valide or empty!");
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

    //TODO Made POsts and then check this out !
//    @PostMapping(value = "posts")
//    public void upploadImageToPost(@RequestParam("URL") String url,
//                                   @RequestParam("postId") int postId,
//                                   HttpSession session) throws NotLoggedException {
//        validateLogged(session);
//        if (url.isEmpty() || url == null) {
//            throw new NullPointerException("URL is not valide or empty!");
//        }
//        String base64 = url;
//       Post post=daoP.getPostById(postId);
//        byte[] bytes = Base64.getDecoder().decode(base64);
//        String fileName = post.getPostID() + FILE_NAME;
//        File newFile = new File(FILE_PATH + fileName);
//        try (FileOutputStream fos = new FileOutputStream(newFile)) {
//            fos.write(bytes);
//            post.setContentURL(newFile.getName());
//            daoP.uploadImage(post);
//        }catch (IOException e){
//            System.out.println("Error in uploading post image!");
//        }
//    }

    @GetMapping(value = "/{name}", produces = "image/png")
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


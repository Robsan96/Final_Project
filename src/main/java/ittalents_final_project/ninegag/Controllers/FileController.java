package ittalents_final_project.ninegag.Controllers;

import ittalents_final_project.ninegag.Models.DAO.PostDAO;
import ittalents_final_project.ninegag.Models.DAO.SectionDAO;
import ittalents_final_project.ninegag.Models.DAO.TagDAO;
import ittalents_final_project.ninegag.Models.DAO.UserDAOImplem;
import ittalents_final_project.ninegag.Models.DTO.RequestPostDTO;
import ittalents_final_project.ninegag.Models.DTO.ResponsePostDTO;
import ittalents_final_project.ninegag.Models.POJO.User;
import ittalents_final_project.ninegag.Utils.Exceptions.BadParamException;
import ittalents_final_project.ninegag.Utils.Exceptions.EmptyParameterException;
import ittalents_final_project.ninegag.Utils.Exceptions.NotLoggedException;
import org.apache.log4j.Logger;
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
    @Autowired
    SectionDAO daoS;

    static Logger log = Logger.getLogger(FileController.class.getName());


    private static final String FILE_PATH = "C:\\Users\\NN\\Desktop\\Pictures\\";

    public static final String FILE_NAME = System.currentTimeMillis() + ".jpg";

    @PostMapping(value = "/images/profiles")
    public void upploadImageToProfile(@RequestParam(value = "URL") String url, HttpSession session)
            throws NotLoggedException {
        validateLogged(session);
        if (url.isEmpty() || url == null) {
            throw new NullPointerException("URL is not valid or empty!");
        }
        byte[] base64 = url.getBytes();
        User user = (User) session.getAttribute(LOGGED);
        String encoded = Base64.getEncoder().encodeToString(base64);
        base64 = Base64.getDecoder().decode(encoded);
        String fileName = user.getEmail() + FILE_NAME;
        File newFile = new File(FILE_PATH + fileName);
        try (FileOutputStream fos = new FileOutputStream(newFile)) {
            fos.write(base64);
            user.setAvatar(newFile.getName());

            daoU.updateUserByID(user);

        } catch (IOException e) {
            log.error(e.getMessage());
            System.out.println("Error in uploading avatar!");
        }
    }

    @PostMapping(value = "posts/add")
    @Transactional
    public ResponsePostDTO upploadImageToPost(@RequestBody RequestPostDTO postDTO, HttpSession session)
            throws NotLoggedException, IOException, BadParamException {
        validateLogged(session);
        User user = (User) session.getAttribute(LOGGED);
        postDTO.setProfileID(user.getUser_ID());
        if (postDTO.getContentURL()==null || postDTO.getContentURL().isEmpty() ) {
            throw new BadParamException("URL is null or empty!");
        }
        if (postDTO.getTitle() == null) {
            throw new BadParamException("Title cannot be null !");
        }
        String title=postDTO.getTitle().replace(" ","");
        if(title.isEmpty()|| title.length()<5){
            throw new BadParamException("Invalid title, must contain at least 5 symbols which are not spaces");
        }
        if (daoS.getById(postDTO.getSectionID()) == null) {
            throw new BadParamException("Section with that ID does not exist !");
        }
        postDTO.setContentURL(CreateImage(postDTO));
        int postId = daoP.addPost(postDTO);
        if (postDTO.getTags().size() > 0 || postDTO.getTags() != null) {
            daoT.setTags(postId, postDTO.getTags());
        }
        return daoP.getBPostDTO(postId,true);
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
            throw new IOException("Error in uploading post image or profile avatar!");
        }
    }

    @GetMapping(value = "/images/{name}", produces = "image/png")
    public byte[] downloadImage(@PathVariable(value = "name") String imageName) throws Exception {
        File newFile = new File(FILE_PATH + imageName + ".jpg");
        if (!newFile.exists()) {
            throw new EmptyParameterException("Oop file with that name does not exist !");
        }
        try {
            byte[] bytes = Files.readAllBytes(newFile.toPath());
            return bytes;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new Exception("Something rly got out of hand ! this msg shoud not be displayed ever !");
        }
    }
}


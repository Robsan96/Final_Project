package ittalents_final_project.ninegag.Controllers;

import ittalents_final_project.ninegag.Models.DAO.GenderDAOImplem;
import ittalents_final_project.ninegag.Models.POJO.Gender;
import ittalents_final_project.ninegag.Utils.Exceptions.NotLoggedException;
import ittalents_final_project.ninegag.Utils.Exceptions.PermitionDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
@RequestMapping(value = "/gender")
public class GenderController extends BaseController {

    @Autowired
    GenderDAOImplem dao;

    @PostMapping(value = "/add")
    public void addGender(@RequestBody Gender gender, HttpSession session) throws NotLoggedException, PermitionDeniedException{
        if (validateAdmin(session)) {
            dao.addGender(gender);
        } else {
            throw new PermitionDeniedException("You are not admin");
        }
    }

    @DeleteMapping(value = "/delete")
    public void removeGender(@RequestBody Gender gender, HttpSession session) throws NotLoggedException, PermitionDeniedException{
        if (validateAdmin(session)) {
            dao.deleteGenderByID(gender.getGender_ID());
        } else {
            throw new PermitionDeniedException("You are not admin");
        }

    }

}

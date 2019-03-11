package ittalents_final_project.ninegag.Controllers;

import ittalents_final_project.ninegag.Models.DAO.GenderDAOImplem;
import ittalents_final_project.ninegag.Models.POJO.Gender;
import ittalents_final_project.ninegag.Utils.Exceptions.BadParamException;
import ittalents_final_project.ninegag.Utils.Exceptions.NotLoggedException;
import ittalents_final_project.ninegag.Utils.Exceptions.PermitionDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/gender")
public class GenderController extends BaseController {

    @Autowired
    GenderDAOImplem dao;

    @PostMapping(value = "/add")
    public String addGender(@RequestBody Gender gender, HttpSession session) throws NotLoggedException, PermitionDeniedException,BadParamException{
        if (validateAdmin(session)) {
            if (dao.getByType(gender.getGender_Type()) != null) {
                throw new BadParamException("There is already such gender in the list.");
            }
            dao.addGender(gender);
            return "Gender added.";
        } else {
            throw new PermitionDeniedException("You are not admin");
        }
    }

    @DeleteMapping(value = "/delete")
    public String removeGender(@RequestBody Gender gender, HttpSession session) throws NotLoggedException, PermitionDeniedException{
        if (validateAdmin(session)) {
            if(dao.getById(gender.getGender_ID())==null){
                throw new NullPointerException("There is no such ID in the list.");
            }
            else{
                dao.deleteGenderByID(gender.getGender_ID());
                return "Gender deleted.";
            }
        } else {
            throw new PermitionDeniedException("You are not an admin");
        }

    }

}

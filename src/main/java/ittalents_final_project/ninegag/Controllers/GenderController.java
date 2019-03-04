package ittalents_final_project.ninegag.Controllers;

import ittalents_final_project.ninegag.Models.DAO.GenderDAOImplem;
import ittalents_final_project.ninegag.Models.POJO.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GenderController extends BaseController {

    @Autowired
    GenderDAOImplem dao;


    @PostMapping(value = "/addGender")
    public void addGender(@RequestBody Gender gender){
        dao.addGender(gender);
    }

    @PostMapping(value = "/deleteGender")
    public void removeGender(@RequestBody Gender gender){
        dao.deleteGenderByID(gender.getGender_ID());
    }

}

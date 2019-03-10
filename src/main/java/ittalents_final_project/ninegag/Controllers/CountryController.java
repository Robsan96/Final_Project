package ittalents_final_project.ninegag.Controllers;

import ittalents_final_project.ninegag.Models.DAO.CountryDAOImplem;
import ittalents_final_project.ninegag.Models.POJO.Country;
import ittalents_final_project.ninegag.Utils.Exceptions.NotLoggedException;
import ittalents_final_project.ninegag.Utils.Exceptions.PermitionDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/country")
public class CountryController extends BaseController{

    @Autowired
    CountryDAOImplem dao;


    @PostMapping(value = "/add")
    public void addCountry(@RequestBody Country country, HttpSession session) throws NotLoggedException, PermitionDeniedException {
        if (validateAdmin(session)) {
            dao.addCountry(country);
        } else {
            throw new PermitionDeniedException("You are not admin");
        }
    }

    @DeleteMapping(value = "/delete")
    public void removeCountry(@RequestBody Country country, HttpSession session) throws NotLoggedException, PermitionDeniedException {
        if (validateAdmin(session)) {
            dao.deleteCountryByID(country.getCountry_ID());
        } else {
            throw new PermitionDeniedException("You are not admin");
        }
    }
}

package ittalents_final_project.ninegag.Controllers;

import ittalents_final_project.ninegag.Models.DAO.CountryDAOImplem;
import ittalents_final_project.ninegag.Models.POJO.Country;
import ittalents_final_project.ninegag.Utils.Exceptions.BadParamException;
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
    public String addCountry(@RequestBody Country country, HttpSession session) throws NotLoggedException, PermitionDeniedException, BadParamException {
        if (validateAdmin(session)) {
            if (dao.getByName(country.getCountry_name()) != null) {
                throw new BadParamException("There is already such a country in the list.");
            }
            dao.addCountry(country);
            return "Country added.";
        } else {
            throw new PermitionDeniedException("You are not admin");
        }
    }

    @DeleteMapping(value = "/delete")
    public String removeCountry(@RequestBody Country country, HttpSession session) throws NotLoggedException, PermitionDeniedException {
        if (validateAdmin(session)) {
            if(dao.getById(country.getCountry_ID())==null){
                throw new NullPointerException("There is no such country in the list.");
            }
            else{
                dao.deleteCountryByID(country.getCountry_ID());
                return "Country deleted.";
            }
        } else {
            throw new PermitionDeniedException("You are not an admin");
        }
    }
}

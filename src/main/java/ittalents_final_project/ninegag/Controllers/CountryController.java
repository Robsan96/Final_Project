package ittalents_final_project.ninegag.Controllers;

import ittalents_final_project.ninegag.Models.DAO.CountryDAOImplem;
import ittalents_final_project.ninegag.Models.POJO.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountryController {

    @Autowired
    CountryDAOImplem dao;


    @PostMapping(value = "/addCountry")
    public void addCountry(@RequestBody Country country){
        dao.addCountry(country);
    }

    @PostMapping(value = "/deleteCountry")
    public void removeCountry(@RequestBody Country country){
        dao.deleteCountryByID(country.getCountry_ID());
    }
}

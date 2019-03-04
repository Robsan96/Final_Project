package ittalents_final_project.ninegag.Models.DAO;


import ittalents_final_project.ninegag.Models.POJO.Country;

public interface CountryDAO {

    public Country getByName(String name);

    public Country getById(int country_ID);

    public void addCountry(Country country);

    public void deleteCountryByID(int country_ID);
}

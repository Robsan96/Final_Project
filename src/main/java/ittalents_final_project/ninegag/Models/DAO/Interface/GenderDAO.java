package ittalents_final_project.ninegag.Models.DAO.Interface;

import ittalents_final_project.ninegag.Models.POJO.Gender;

public interface GenderDAO {

    public Gender getByType(String name);

    public Gender getById(int gender_ID);

    public void addGender(Gender gender);

    public void deleteGenderByID(int gender_ID);
}

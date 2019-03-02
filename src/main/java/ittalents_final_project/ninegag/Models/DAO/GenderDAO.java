package ittalents_final_project.ninegag.Models.DAO;

import ittalents_final_project.ninegag.Models.POJO.Gender;

public interface GenderDAO {

    public Gender getByType(String name);

    public Gender getById(int gender_ID);

    public void addGender(Gender gender);
}

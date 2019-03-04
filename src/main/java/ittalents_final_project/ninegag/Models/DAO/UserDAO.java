package ittalents_final_project.ninegag.Models.DAO;

import ittalents_final_project.ninegag.Models.POJO.User;

public interface UserDAO {

    public void addUser(User user);

    public void updateUserByID(User user);

    public void updateUserByEmail(User user);

    public void deleteUserByID(long id);

    public void deleteUserByEmail(String email);

    public Object findUserByEmail(String email);

    public Object findUserByID(long profile_id);
}

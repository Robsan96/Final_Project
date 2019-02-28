package ittalents_final_project.ninegag.Models.DAO;

import ittalents_final_project.ninegag.Models.POJO.User;

public interface UserDAO {

    public void addUser(User user);

    public void updateUser(User user);

    public void deleteUser(long id);

    public Object findUserByEmailAndPassword(String email, String password);

    public Object findUserByID(long profile_id);
}

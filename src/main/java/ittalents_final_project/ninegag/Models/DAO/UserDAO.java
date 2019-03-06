package ittalents_final_project.ninegag.Models.DAO;

import ittalents_final_project.ninegag.Models.DTO.UserCommentsDTO;
import ittalents_final_project.ninegag.Models.DTO.UserPostsDTO;
import ittalents_final_project.ninegag.Models.DTO.UserUpvotesDTO;
import ittalents_final_project.ninegag.Models.POJO.User;

public interface UserDAO {

    public void addUser(User user);

    public void updateUserByID(User user);

    public void updateUserByEmail(User user);

    public void deleteUserByID(int user_id);

    public void deleteUserByEmail(String email);

    public Object findUserByEmail(String email);

    public Object findUserByID(int user_id);

    public UserCommentsDTO getUserCommentedPosts(int user_ID);

    public UserPostsDTO getUserPosts(int user_ID);

    public UserUpvotesDTO getUserUpvotedPosts(int user_ID);
}

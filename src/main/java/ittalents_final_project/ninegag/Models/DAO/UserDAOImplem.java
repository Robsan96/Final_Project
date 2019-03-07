package ittalents_final_project.ninegag.Models.DAO;

import ittalents_final_project.ninegag.Models.DTO.UserCommentsDTO;
import ittalents_final_project.ninegag.Models.DTO.UserPostsDTO;
import ittalents_final_project.ninegag.Models.DTO.UserUpvotesDTO;
import ittalents_final_project.ninegag.Models.POJO.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserDAOImplem implements UserDAO {

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    PostDAO dao;


    @Autowired
    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private SqlParameterSource getSqlParameterByModel(User user){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        if(user != null){
            parameterSource.addValue("user_ID", user.getUser_ID());
            parameterSource.addValue("email", user.getEmail());
            parameterSource.addValue("username", user.getUsername());
            parameterSource.addValue("password", user.getPassword());
            parameterSource.addValue("salt", user.getSalt());
            parameterSource.addValue("full_name", user.getFull_name());
            parameterSource.addValue("birthday", user.getBirthday());
            parameterSource.addValue("gender_ID", user.getGender_ID());
            parameterSource.addValue("country_ID", user.getCountry_ID());
            parameterSource.addValue("description", user.getDescription());
            parameterSource.addValue("facebook_account", user.getFacebook_account());
            parameterSource.addValue("google_account", user.getGoogle_account());
            parameterSource.addValue("avatar", user.getAvatar());
            parameterSource.addValue("sensitive_filter", user.isSensitive_filter());
            parameterSource.addValue("admin_privileges", user.isAdmin_privileges());
        }
        return parameterSource;
    }

    private static final class UserMapper implements RowMapper {

        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUser_ID(rs.getInt("user_ID"));
            user.setEmail(rs.getString("email"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setSalt(rs.getString("salt"));
            user.setFull_name(rs.getString("full_name"));
            user.setBirthday(rs.getDate("birthday"));
            user.setGender_ID(rs.getInt("gender_ID"));
            user.setCountry_ID(rs.getInt("country_ID"));
            user.setDescription(rs.getString("description"));
            user.setFacebook_account(rs.getString("facebook_account"));
            user.setGoogle_account(rs.getString("google_account"));
            user.setAvatar(rs.getString("avatar"));
            user.setSensitive_filter(rs.getBoolean("sensitive_filter"));
            user.setAdmin_privileges(rs.getBoolean("admin_privileges"));

            return user;
        }

    }

    private static final class UserPostDTOMapper implements RowMapper {

        public UserPostsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserPostsDTO userPostsDTO = new UserPostsDTO();
            userPostsDTO.setUser_ID(rs.getInt("user_ID"));
            userPostsDTO.setEmail(rs.getString("email"));
            userPostsDTO.setUsername(rs.getString("username"));
            userPostsDTO.setPassword(rs.getString("password"));
            userPostsDTO.setSalt(rs.getString("salt"));
            userPostsDTO.setFull_name(rs.getString("full_name"));
            userPostsDTO.setBirthday(rs.getDate("birthday"));
            userPostsDTO.setGender_ID(rs.getInt("gender_ID"));
            userPostsDTO.setCountry_ID(rs.getInt("country_ID"));
            userPostsDTO.setDescription(rs.getString("description"));
            userPostsDTO.setFacebook_account(rs.getString("facebook_account"));
            userPostsDTO.setGoogle_account(rs.getString("google_account"));
            userPostsDTO.setAvatar(rs.getString("avatar"));
            userPostsDTO.setSensitive_filter(rs.getBoolean("sensitive_filter"));
            userPostsDTO.setAdmin_privileges(rs.getBoolean("admin_privileges"));

            return userPostsDTO;
        }
    }

    private static final class UserUpvotesDTOMapper implements RowMapper {

        public UserUpvotesDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserUpvotesDTO userUpvotesDTO = new UserUpvotesDTO();
            userUpvotesDTO.setUser_ID(rs.getInt("user_ID"));
            userUpvotesDTO.setEmail(rs.getString("email"));
            userUpvotesDTO.setUsername(rs.getString("username"));
            userUpvotesDTO.setPassword(rs.getString("password"));
            userUpvotesDTO.setSalt(rs.getString("salt"));
            userUpvotesDTO.setFull_name(rs.getString("full_name"));
            userUpvotesDTO.setBirthday(rs.getDate("birthday"));
            userUpvotesDTO.setGender_ID(rs.getInt("gender_ID"));
            userUpvotesDTO.setCountry_ID(rs.getInt("country_ID"));
            userUpvotesDTO.setDescription(rs.getString("description"));
            userUpvotesDTO.setFacebook_account(rs.getString("facebook_account"));
            userUpvotesDTO.setGoogle_account(rs.getString("google_account"));
            userUpvotesDTO.setAvatar(rs.getString("avatar"));
            userUpvotesDTO.setSensitive_filter(rs.getBoolean("sensitive_filter"));
            userUpvotesDTO.setAdmin_privileges(rs.getBoolean("admin_privileges"));

            return userUpvotesDTO;
        }
    }

    private static final class UserCommentsDTOMapper implements RowMapper {

        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserCommentsDTO userCommentsDTO = new UserCommentsDTO();
            userCommentsDTO.setUser_ID(rs.getInt("user_ID"));
            userCommentsDTO.setEmail(rs.getString("email"));
            userCommentsDTO.setUsername(rs.getString("username"));
            userCommentsDTO.setPassword(rs.getString("password"));
            userCommentsDTO.setSalt(rs.getString("salt"));
            userCommentsDTO.setFull_name(rs.getString("full_name"));
            userCommentsDTO.setBirthday(rs.getDate("birthday"));
            userCommentsDTO.setGender_ID(rs.getInt("gender_ID"));
            userCommentsDTO.setCountry_ID(rs.getInt("country_ID"));
            userCommentsDTO.setDescription(rs.getString("description"));
            userCommentsDTO.setFacebook_account(rs.getString("facebook_account"));
            userCommentsDTO.setGoogle_account(rs.getString("google_account"));
            userCommentsDTO.setAvatar(rs.getString("avatar"));
            userCommentsDTO.setSensitive_filter(rs.getBoolean("sensitive_filter"));
            userCommentsDTO.setAdmin_privileges(rs.getBoolean("admin_privileges"));

            return userCommentsDTO;
        }
    }

    public void addUser(User user) {
        String sql = "INSERT INTO users(email,username,password,salt) VALUES(:email, :username, :password, :salt)";

        namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(user));
    }

    public void updateUserByID(User user) {
        String sql = "UPDATE users SET full_name = :full_name, avatar = :avatar, gender_ID = :gender_ID, birthday = :birthday, country_ID = :country_ID, facebook_account = :facebook_account, google_account = :google_account  WHERE user_ID = :user_ID";

        namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(user));
    }

    public void updateUserByEmail(User user) {
        String sql = "UPDATE users SET full_name = :full_name, avatar = :avatar, gender_ID = :gender_ID, birthday = :birthday, country_ID = :country_ID, facebook_account = :facebook_account, google_account = :google_account  WHERE email = :email";

        namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(user));
    }

    public void deleteUserByID(int user_ID) {
        String sql = "DELETE FROM users WHERE user_ID = :user_ID";

        namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(new User(user_ID)));
    }

    public void deleteUserByEmail(String email) {
        String sql = "DELETE FROM users WHERE email = :email";

        namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(new User(email)));
    }

    public User findUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = :email";

        return (User)namedParameterJdbcTemplate.queryForObject(sql, getSqlParameterByModel(new User(email)), new UserMapper());
    }

    public Object findUserByID(int user_ID) {
        String sql = "SELECT * FROM users WHERE user_ID = :user_ID";

        return namedParameterJdbcTemplate.queryForObject(sql, getSqlParameterByModel(new User(user_ID)), new UserMapper());
    }

    public UserCommentsDTO getUserCommentedPosts(int user_ID){
        try{
            String sql = "SELECT * FROM users WHERE user_ID = :user_ID";
            UserCommentsDTO userCommentedPosts = (UserCommentsDTO)namedParameterJdbcTemplate.queryForObject(sql, getSqlParameterByModel(new UserCommentsDTO(user_ID)), new UserCommentsDTOMapper());
            userCommentedPosts.setCommentedPosts(dao.getAllPostsCommentedBy(userCommentedPosts.getUser_ID()));
            return userCommentedPosts;
        }
        catch (
            EmptyResultDataAccessException e) {
            return null;
        }
    }

    public UserPostsDTO getUserPosts(int user_ID){
        try{
            String sql = "SELECT * FROM users WHERE user_ID = :user_ID";
            UserPostsDTO userPosts = (UserPostsDTO)namedParameterJdbcTemplate.queryForObject(sql, getSqlParameterByModel(new UserPostsDTO(user_ID)), new UserPostDTOMapper());
            userPosts.setUploadedPosts(dao.getAllPostsByUser(userPosts.getUser_ID()));
            return userPosts;
        }
        catch (
                EmptyResultDataAccessException e) {
            return null;
        }
    }

    public UserUpvotesDTO getUserUpvotedPosts(int user_ID){
        try{
            String sql = "SELECT * FROM users WHERE user_ID = :user_ID";
            UserUpvotesDTO userUpvotes = (UserUpvotesDTO)namedParameterJdbcTemplate.queryForObject(sql, getSqlParameterByModel(new UserUpvotesDTO(user_ID)), new UserUpvotesDTOMapper());
            userUpvotes.setLikedPosts(dao.getAllPostsVotedBy(userUpvotes.getUser_ID()));
            return userUpvotes;
        }
        catch (
                EmptyResultDataAccessException e) {
            return null;
        }
    }
}

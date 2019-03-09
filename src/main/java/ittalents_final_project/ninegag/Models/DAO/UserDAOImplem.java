package ittalents_final_project.ninegag.Models.DAO;

import ittalents_final_project.ninegag.Models.DTO.*;
import ittalents_final_project.ninegag.Models.POJO.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDAOImplem implements UserDAO {

    static Logger log = Logger.getLogger(UserDAOImplem.class.getName());

    NamedParameterJdbcTemplate JdbcTemplate;
    @Autowired
    PostDAO postDAO;
    @Autowired
    CommentDAO commentDAO;


    @Autowired
    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.JdbcTemplate = jdbcTemplate;
    }

    public void addUser(User user) {
        String sql = "INSERT INTO users(email,username,password,salt) VALUES(:email, :username, :password, :salt)";

        JdbcTemplate.update(sql, getSqlParameterByModel(user));
    }

    public void updateUserByID(User user) {
        String sql = "UPDATE users SET full_name = :full_name, avatar = :avatar, gender_ID = :gender_ID, birthday = :birthday," +
                " country_ID = :country_ID, facebook_account = :facebook_account, google_account = :google_account  WHERE user_ID = :user_ID";

        JdbcTemplate.update(sql, getSqlParameterByModel(user));
    }

    public void updateUserByEmail(User user) {
        String sql = "UPDATE users SET full_name = :full_name, avatar = :avatar, gender_ID = :gender_ID, birthday = :birthday," +
                " country_ID = :country_ID, facebook_account = :facebook_account, google_account = :google_account  WHERE email = :email";

        JdbcTemplate.update(sql, getSqlParameterByModel(user));
    }

    public void deleteUserByID(int user_ID) {
        JdbcTemplate.update("UPDATE users SET full_name = 'DELETED', email = 'DELETED', username = 'DELETED' WHERE user_ID = :user_ID", getSqlParameterByModel(new User(user_ID)));
    }

//    @Transactional
//    public void deleteUserByID(int user_ID) {
//        deleteCommentsByUser(user_ID);
//        deletePostsByUser(user_ID);
//
//        JdbcTemplate.update("DELETE FROM users WHERE user_ID = :user_ID", getSqlParameterByModel(new User(user_ID)));
//    }
//
//    public void deleteCommentsByUser(int user_ID){
//        JdbcTemplate.update("DELETE FROM comments_likes WHERE profile_ID= :profile_ID", getSqlParameterByModel(new CommentLikes(user_ID)));
//        JdbcTemplate.update("DELETE FROM comments WHERE reply_of_ID= :reply_of_ID AND profile_ID= :profile_ID", getSqlParameterByModel(new Comment(user_ID,user_ID)));
//    }
//
//    public void deletePostsByUser(int user_ID){
//        List<ResponsePostDTO> posts = postDAO.getAllPostsByUser(user_ID);
//        List<Integer> postID = new ArrayList();
//        for (ResponsePostDTO r: posts){
//            postID.add(r.getPostID());
//        }
//        JdbcTemplate.update("DELETE FROM post_likes WHERE profile_id= :profile_id", getSqlParameterByModel(new PostLikes(user_ID)));
//        for (Integer i: postID){
//            String sql = "DELETE FROM post_tags WHERE post_id= :post_id";
//            JdbcTemplate.update(sql, getSqlParameterByModel(new PostTag(i)));
//        }
//        JdbcTemplate.update("DELETE FROM posts WHERE user_ID= :user_ID", getSqlParameterByModel(new Post(user_ID)));
//    }

    public void deleteUserByEmail(String email) {
        String sql = "DELETE FROM users WHERE email = :email";

        JdbcTemplate.update(sql, getSqlParameterByModel(new User(email)));
    }

    public User findUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = :email";
        return (User) JdbcTemplate.queryForObject(sql, getSqlParameterByModel(new User(email)), new UserMapper());
    }

    public User findUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = :username";

        return (User) JdbcTemplate.queryForObject(sql, getSqlParameterByModel(new User(username)), new UserMapper());
    }

    public User findUserByID(int user_ID) {
        String sql = "SELECT * FROM users WHERE user_ID = :user_ID";

        return (User) JdbcTemplate.queryForObject(sql, getSqlParameterByModel(new User(user_ID)), new UserMapper());
    }

    public UserCommentsDTO getUserCommentedPosts(int user_ID){
        try{
            String sql = "SELECT * FROM users WHERE user_ID = :user_ID";
            UserCommentsDTO userCommentedPosts = (UserCommentsDTO) JdbcTemplate.queryForObject(sql, getSqlParameterByModel(new UserCommentsDTO(user_ID)), new UserCommentsDTOMapper());
            userCommentedPosts.setCommentedPosts(postDAO.getAllPostsCommentedBy(userCommentedPosts.getUser_ID()));
            return userCommentedPosts;
        }
        catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public UserPostsDTO getUserPosts(int user_ID){
        try{
            String sql = "SELECT * FROM users WHERE user_ID = :user_ID";
            UserPostsDTO userPosts = (UserPostsDTO) JdbcTemplate.queryForObject(sql, getSqlParameterByModel(new UserPostsDTO(user_ID)), new UserPostDTOMapper());
            userPosts.setUploadedPosts(postDAO.getAllPostsByUser(userPosts.getUser_ID()));
            return userPosts;
        }
        catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public UserUpvotesDTO getUserUpvotedPosts(int user_ID){
        try{
            String sql = "SELECT * FROM users WHERE user_ID = :user_ID";
            UserUpvotesDTO userUpvotes = (UserUpvotesDTO) JdbcTemplate.queryForObject(sql, getSqlParameterByModel(new UserUpvotesDTO(user_ID)), new UserUpvotesDTOMapper());
            userUpvotes.setLikedPosts(postDAO.getAllPostsVotedBy(userUpvotes.getUser_ID()));
            return userUpvotes;
        }
        catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public UserDTO getUserInfo(String email){
        String sql = "SELECT * FROM users WHERE email = :email";
        UserDTO userDTO = (UserDTO) JdbcTemplate.queryForObject(sql, getSqlParameterByModel(new UserDTO(email)), new UserDTOMapper());
        return  userDTO;
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

    private SqlParameterSource getSqlParameterByModel(UserDTO userDTO){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        if(userDTO != null){
            parameterSource.addValue("user_ID", userDTO.getUser_ID());
            parameterSource.addValue("email", userDTO.getEmail());
            parameterSource.addValue("username", userDTO.getUsername());
            parameterSource.addValue("full_name", userDTO.getFull_name());
            parameterSource.addValue("birthday", userDTO.getBirthday());
            parameterSource.addValue("gender_ID", userDTO.getGender_ID());
            parameterSource.addValue("country_ID", userDTO.getCountry_ID());
            parameterSource.addValue("description", userDTO.getDescription());
            parameterSource.addValue("facebook_account", userDTO.getFacebook_account());
            parameterSource.addValue("google_account", userDTO.getGoogle_account());
            parameterSource.addValue("avatar", userDTO.getAvatar());
            parameterSource.addValue("sensitive_filter", userDTO.isSensitive_filter());
        }
        return parameterSource;
    }

    private SqlParameterSource getSqlParameterByModel(Post post){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        if(post != null){
            parameterSource.addValue("postID", post.getPostID());
            parameterSource.addValue("user_ID", post.getPostID());
        }
        return parameterSource;
    }

    private SqlParameterSource getSqlParameterByModel(CommentLikes commentLikes){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        if(commentLikes != null){
            parameterSource.addValue("profile_ID", commentLikes.getProfile_ID());
        }
        return parameterSource;
    }

    private SqlParameterSource getSqlParameterByModel(Comment comment){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        if(comment != null){
            parameterSource.addValue("profile_ID", comment.getId());
            parameterSource.addValue("reply_of_ID", comment.getReply());
        }
        return parameterSource;
    }

    private SqlParameterSource getSqlParameterByModel(PostLikes postLikes){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        if(postLikes != null){
            parameterSource.addValue("profile_id", postLikes.getProfile_ID());
        }
        return parameterSource;
    }

    private SqlParameterSource getSqlParameterByModel(PostTag postTag){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        if(postTag != null){
            parameterSource.addValue("post_id", postTag.getPostID());
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

    private static final class UserDTOMapper implements RowMapper {

        public UserDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserDTO userDTO = new UserDTO();
            userDTO.setUser_ID(rs.getInt("user_ID"));
            userDTO.setEmail(rs.getString("email"));
            userDTO.setUsername(rs.getString("username"));
            userDTO.setFull_name(rs.getString("full_name"));
            userDTO.setBirthday(rs.getDate("birthday"));
            userDTO.setGender_ID(rs.getInt("gender_ID"));
            userDTO.setCountry_ID(rs.getInt("country_ID"));
            userDTO.setDescription(rs.getString("description"));
            userDTO.setFacebook_account(rs.getString("facebook_account"));
            userDTO.setGoogle_account(rs.getString("google_account"));
            userDTO.setAvatar(rs.getString("avatar"));
            userDTO.setSensitive_filter(rs.getBoolean("sensitive_filter"));

            return userDTO;
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
}

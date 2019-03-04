package ittalents_final_project.ninegag.Models.DAO;

import ittalents_final_project.ninegag.Models.POJO.User;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final class ProfileMapper implements RowMapper {

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

    public void deleteUserByID(long user_ID) {
        String sql = "DELETE FROM users WHERE user_ID = :user_ID";

        namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(new User(user_ID)));
    }

    public void deleteUserByEmail(String email) {
        String sql = "DELETE FROM users WHERE email = :email";

        namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(new User(email)));
    }

    public User findUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = :email";

        return (User)namedParameterJdbcTemplate.queryForObject(sql, getSqlParameterByModel(new User(email)), new ProfileMapper());
    }

    public Object findUserByID(long user_ID) {
        String sql = "SELECT * FROM users WHERE user_ID = :user_ID";

        return namedParameterJdbcTemplate.queryForObject(sql, getSqlParameterByModel(new User(user_ID)), new ProfileMapper());
    }
}

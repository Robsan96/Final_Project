package ittalents_final_project.ninegag.Models.DAO;

import ittalents_final_project.ninegag.Models.POJO.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GenderDAOImplem implements GenderDAO {

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Gender getByType(String gender_Type) {
        String sql = "SELECT * FROM genders WHERE gender_Type = :gender_Type";

        return (Gender) namedParameterJdbcTemplate.queryForObject(sql, getSqlParameterByModel(new Gender(gender_Type)), new GenderDAOImplem.GenderMapper());
    }

    @Override
    public Gender getById(int gender_ID) {
        String sql = "SELECT * FROM genders WHERE gender_ID = :gender_ID";

        return (Gender) namedParameterJdbcTemplate.queryForObject(sql, getSqlParameterByModel(new Gender(gender_ID)), new GenderDAOImplem.GenderMapper());
    }

    @Override
    public void addGender(Gender gender) {
        String sql = "INSERT INTO genders(gender_Type) VALUES(:gender_Type)";

        namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(gender));
    }

    private SqlParameterSource getSqlParameterByModel(Gender gender){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        if(gender != null){
            parameterSource.addValue("gender_ID", gender.getGender_ID());
            parameterSource.addValue("gender_Type", gender.getGender_Type());
        }
        return parameterSource;
    }

    private static final class GenderMapper implements RowMapper {

        public Gender mapRow(ResultSet rs, int rowNum) throws SQLException {
            Gender gender = new Gender();
            gender.setGender_ID(rs.getInt("gender_ID"));
            gender.setGender_Type(rs.getString("gender_Type"));

            return gender;
        }

    }
}

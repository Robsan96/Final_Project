package ittalents_final_project.ninegag.Models.DAO;

import ittalents_final_project.ninegag.Models.POJO.Country;
import ittalents_final_project.ninegag.Models.POJO.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CountryDAOImplem implements CountryDAO {

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    org.springframework.jdbc.core.JdbcTemplate jdbc;

    @Autowired
    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Country getByName(String country_name) {
        try {
            String sql = "SELECT country_ID, country_name FROM countries WHERE country_name = ?";

            return (Country) jdbc.queryForObject(sql, new Object[]{country_name}, new CountryDAOImplem.CountryMapper());
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public Country getById(int country_ID) {
                try {
            String sql = "SELECT country_ID, country_name FROM countries WHERE country_ID = ?";

            return (Country) jdbc.queryForObject(sql, new Object[]{country_ID}, new CountryDAOImplem.CountryMapper());
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public void addCountry(Country country) {
        String sql = "INSERT INTO countries(country_name) VALUES(:country_name)";

        namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(country));
    }

    private SqlParameterSource getSqlParameterByModel(Country country){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        if(country != null){
            parameterSource.addValue("country_ID", country.getCountry_ID());
            parameterSource.addValue("country_name", country.getCountry_name());
        }
        return parameterSource;
    }

    private static final class CountryMapper implements RowMapper {

        public Country mapRow(ResultSet rs, int rowNum) throws SQLException {
            Country country = new Country();
            country.setCountry_ID(rs.getInt("country_ID"));
            country.setCountry_name(rs.getString("country_name"));

            return country;
        }
    }

    public void deleteCountryByID(int country_ID) {
        String sql = "DELETE FROM countries WHERE country_ID = :country_ID";

        namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(new Country(country_ID)));
    }
}

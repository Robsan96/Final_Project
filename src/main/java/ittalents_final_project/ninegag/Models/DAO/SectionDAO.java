package ittalents_final_project.ninegag.Models.DAO;

import ittalents_final_project.ninegag.Models.POJO.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class SectionDAO {

    @Autowired
    NamedParameterJdbcTemplate jd;
    @Autowired
    JdbcTemplate jdbcTemplate;
    public static final String SQL = "SELECT section_ID , section_name FROM final_project.sections";

    public List<Section> getAll() {
        try {
            return jd.query(SQL, (resultSet, i) -> mapRow(resultSet));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Section getByName(String section_name) {
        try {
            String sql = SQL + " WHERE section_name= ?";
            Section section = jdbcTemplate.queryForObject(sql, new Object[]{section_name}, (resultSet, i) -> mapRow(resultSet));
            return section;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Section getById(int id) {
        try {
            String sql = SQL + " WHERE section_ID= ?";
            Section section = jdbcTemplate.queryForObject(sql, new Object[]{id}, (resultSet, i) -> mapRow(resultSet));
            return section;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private Section mapRow(ResultSet rs) throws SQLException {
       return new Section(rs.getInt("section_ID"),rs.getString("section_name"));
    }

    public int addSection(String name) {
        String sql = "INSERT INTO sections(section_name) VALUES(?)";
        return jdbcTemplate.update(sql, new Object[]{name});
    }
}


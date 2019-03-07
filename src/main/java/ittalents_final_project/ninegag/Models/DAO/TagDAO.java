package ittalents_final_project.ninegag.Models.DAO;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ittalents_final_project.ninegag.Models.POJO.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagDAO {

    static Logger log = Logger.getLogger(TagDAO.class.getName());

    @Autowired
    JdbcTemplate jdbcTemplate;
    public static final String SQL = "SELECT tag_ID,tag_name FROM tags";

    public Tag getByName(String name) {
        try {
            String sql = SQL + " WHERE tag_name=?";
            return jdbcTemplate.queryForObject(sql, new Object[]{name}, (resultSet, i) -> mapRow(resultSet));
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public Tag getById(int id) {
        String sql = SQL + " WHERE tag_ID= ? ";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (resultSet, i) -> mapRow(resultSet));
    }

    public int addTag(String tagName) {
        String sql = "INSERT INTO tags(tag_name) VALUES(?)";
        return jdbcTemplate.update(sql, new Object[]{tagName});
    }

    private Tag mapRow(ResultSet rs) throws SQLException {
        return new Tag(rs.getInt("tag_ID"), rs.getString("tag_name"));
    }
}
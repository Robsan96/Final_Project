package ittalents_final_project.ninegag.Models.DAO.Implement;

import ittalents_final_project.ninegag.Models.DAO.Interface.TagDAO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ittalents_final_project.ninegag.Models.POJO.Tag;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Component
public class TagDAOimpl implements TagDAO {

    static Logger log = Logger.getLogger(TagDAOimpl.class.getName());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static final String SQL = "SELECT tag_ID,tag_name FROM tags";

    @Override
    public Tag getByName(String name) {
        try {
            String sql = SQL + " WHERE tag_name=?";
            return jdbcTemplate.queryForObject(sql, new Object[]{name}, (resultSet, i) -> mapRow(resultSet));
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Tag getById(int tagId) {
        try {
            String sql = SQL + " WHERE tag_id=?";
            return jdbcTemplate.queryForObject(sql, new Object[]{tagId}, (resultSet, i) -> mapRow(resultSet));
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Tag> getTagsByPost(int postId) {
        String sql = "SELECT DISTINCT tag_ID,tag_name FROM tags t JOIN post_tags p USING(tag_ID) WHERE p.post_ID=?";
        return jdbcTemplate.query(sql, new Object[]{postId}, (resultSet, i) -> mapRow(resultSet));
    }

    @Override
    public void setTags(int postId, List<Tag> tags) {
        String sql = "INSERT INTO post_tags(tag_id,post_id) VALUES(?,?)";
        for (Tag tag : tags) {
            if (this.getByName(tag.getName()) == null) {
                tag.setTag_id(this.addTag(tag.getName()));
            } else {
                tag = this.getByName(tag.getName());
            }
            jdbcTemplate.update(sql, new Object[]{tag.getTag_id(), postId});
        }
    }

    @Override
    public int addTag(String tagName) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO tags(tag_name) VALUES(?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tagName);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    private Tag mapRow(ResultSet rs) throws SQLException {
        return new Tag(rs.getInt("tag_ID"), rs.getString("tag_name"));
    }
}
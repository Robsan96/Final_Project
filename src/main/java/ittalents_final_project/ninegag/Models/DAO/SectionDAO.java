package ittalents_final_project.ninegag.Models.DAO;

import ittalents_final_project.ninegag.Models.DTO.ResponsePostDTO;
import ittalents_final_project.ninegag.Models.POJO.Post;
import ittalents_final_project.ninegag.Models.POJO.Section;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Component
public class SectionDAO {

    static Logger log = Logger.getLogger(SectionDAO.class.getName());

    @Autowired
    private PostDAO daoP;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public static final String SQL = "SELECT section_id , section_name FROM sections";

    public List<Section> getAll() {
        try {
            return jdbcTemplate.query(SQL, (resultSet, i) -> mapRow(resultSet));
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public Section getByName(String section_name) {
        try {
            String sql = SQL + " WHERE section_name= ?";
            Section section = jdbcTemplate.queryForObject(sql, new Object[]{section_name}, (resultSet, i) -> mapRow(resultSet));
            return section;
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public Section getById(int id) {
        try {
            String sql = SQL + " WHERE section_ID= ?";
            Section section = jdbcTemplate.queryForObject(sql, new Object[]{id}, (resultSet, i) -> mapRow(resultSet));
            return section;
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private Section mapRow(ResultSet rs) throws SQLException {
        return new Section(rs.getInt("section_ID"), rs.getString("section_name"));
    }

    public int addSection(String name) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO sections(section_name) VALUES(?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Transactional
    public int deleteSection(Section section) {
        List<ResponsePostDTO> posts = daoP.getAllPostsBySection(section.getId());
        for (ResponsePostDTO post : posts) {
            daoP.removePost(post);
        }
        jdbcTemplate.update("DELETE FROM sections WHERE section_id=?", new Object[]{section.getId()});
        return section.getId();
    }
}


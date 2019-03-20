package ittalents_final_project.ninegag.Models.DAO;

import ittalents_final_project.ninegag.Models.DTO.RequestPostDTO;
import ittalents_final_project.ninegag.Models.DTO.ResponsePostDTO;
import ittalents_final_project.ninegag.Models.POJO.Comment;
import ittalents_final_project.ninegag.Models.POJO.Post;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class PostDAO {

    static Logger log = Logger.getLogger(PostDAO.class.getName());

    public static final String SQL = "SELECT   p.post_ID, p.title, p.content_URL, p.user_ID, s.section_ID, " +
            "p.date_time_created, p.see_sensitive, p.attribute_poster, (SELECT COUNT(*)" +
            "FROM comments  WHERE post_ID=p.post_ID )AS comments," +
            "(SELECT COUNT(*)-(SELECT COUNT(*)FROM post_likes WHERE post_id=p.post_ID AND status=0)" +
            "FROM post_likes WHERE post_id=p.post_ID AND status=1)AS votes " +
            "FROM posts p JOIN sections s ON (p.section_ID=s.section_ID) ";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private TagDAO tagDAO;

    public Post getPostById(int Id) {
        try {
            String sql = "SELECT post_ID, title, content_URL, user_ID, section_ID, " +
                    "date_time_created, see_sensitive, attribute_poster FROM posts WHERE post_ID=?";
            return jdbcTemplate.queryForObject(sql, new Object[]{Id}, ((resultSet, i) -> mapRow(resultSet)));
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public List<ResponsePostDTO> getAllPostsBy(String orderd) {
        String sql = SQL;
        sql += OrderByMethod(orderd);
        return jdbcTemplate.query(sql, (resultSet, i) -> mapRowBasicDTO(resultSet));
    }

    public ResponsePostDTO getBPostDTO(int Id, boolean showComments) {
        try {
            String sql = SQL + " WHERE post_ID=?";

            ResponsePostDTO post = jdbcTemplate.queryForObject(sql, new Object[]{Id},
                    ((resultSet, i) -> mapRowBasicDTO(resultSet)));
            post.setTags(tagDAO.getTagsByPost(post.getPostID()));
            if (showComments) {
                post.setAllComments(commentDAO.getAllByPostDTO(post.getPostID()));
            }
            return post;
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public List<ResponsePostDTO> getAllPostsByUser(int userId) {
        String sql = SQL + "WHERE user_ID=? ORDER BY  votes DESC ";
        List<ResponsePostDTO> posts = jdbcTemplate.query(sql, new Object[]{userId}, (resultSet, i) -> mapRowBasicDTO(resultSet));
        for (ResponsePostDTO post : posts) {
            post.setTags(tagDAO.getTagsByPost(post.getPostID()));
        }
        return posts;
    }

    public List<ResponsePostDTO> getAllPostsByTag(int tag, String orderd) {
        String sql = SQL + " JOIN post_tags t ON(p.post_ID=t.post_id) WHERE t.tag_id=? ";
        sql += OrderByMethod(orderd);
        List<ResponsePostDTO> posts = jdbcTemplate.query(sql, new Object[]{tag}, (resultSet, i) -> mapRowBasicDTO(resultSet));
        for (ResponsePostDTO post : posts) {
            post.setTags(tagDAO.getTagsByPost(post.getPostID()));
        }
        return posts;
    }

    public List<ResponsePostDTO> getAllPostsBySection(int sectionId, String orderd) {
        String sql = SQL + "WHERE p.section_ID=? ";
        sql += OrderByMethod(orderd);
        List<ResponsePostDTO> posts = jdbcTemplate.query(sql, new Object[]{sectionId},
                (resultSet, i) -> mapRowBasicDTO(resultSet));
        for (ResponsePostDTO post : posts) {
            post.setTags(tagDAO.getTagsByPost(post.getPostID()));
        }
        return posts;
    }

    public List<ResponsePostDTO> getAllPostsCommentedBy(int userId) {
        String sql = "SELECT DISTINCT p.post_ID, p.title, p.content_URL, p.user_ID, s.section_ID," +
                "p.date_time_created, p.see_sensitive, p.attribute_poster, (SELECT COUNT(*)" +
                "FROM comments  WHERE post_ID=p.post_ID )AS comments," +
                "(SELECT COUNT(*)-(SELECT COUNT(*)FROM post_likes WHERE post_id=p.post_ID AND status=0)" +
                "FROM post_likes WHERE post_id=p.post_ID AND status=1)AS votes " +
                "FROM posts p JOIN sections s ON (p.section_ID=s.section_ID) " +
                "JOIN comments c ON(p.post_ID=c.post_ID) WHERE c.profile_ID=? ORDER BY votes DESC ";
        List<ResponsePostDTO> posts = jdbcTemplate.query(sql, new Object[]{userId},
                (resultSet, i) -> mapRowBasicDTO(resultSet));
        for (ResponsePostDTO post : posts) {
            post.setTags(tagDAO.getTagsByPost(post.getPostID()));
        }
        return posts;
    }

    public List<ResponsePostDTO> getAllPostsMadeBy(int userId) {
        String sql = SQL + " WHERE p.user_ID=? ORDER BY votes DESC";
        List<ResponsePostDTO> posts = jdbcTemplate.query(sql, new Object[]{userId},
                (resultSet, i) -> mapRowBasicDTO(resultSet));
        for (ResponsePostDTO post : posts) {
            post.setTags(tagDAO.getTagsByPost(post.getPostID()));
        }
        return posts;
    }

    public List<ResponsePostDTO> getAllPostsVotedBy(int userId) {
        String sql = SQL + " JOIN post_likes l ON (p.post_ID=l.post_id) WHERE l.profile_id=? ORDER BY votes DESC";
        List<ResponsePostDTO> posts = jdbcTemplate.query(sql, new Object[]{userId},
                (resultSet, i) -> mapRowBasicDTO(resultSet));
        for (ResponsePostDTO post : posts) {
            post.setTags(tagDAO.getTagsByPost(post.getPostID()));
        }
        return posts;
    }

    public int addPost(Post post) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO posts(title,content_URL," +
                            "user_ID,section_ID,see_sensitive,attribute_poster) VALUES(?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getContentURL());
            ps.setInt(3, post.getProfileID());
            ps.setInt(4, post.getSectionID());
            ps.setBoolean(5, post.isSeeSensitive());
            ps.setBoolean(6, post.isAtrributePoster());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Transactional(rollbackFor = Exception.class)
    public int removePost(Post post) {
        List<Comment> comments = commentDAO.getAllByPost(post);
        for (Comment comment : comments) {
            commentDAO.deleteComment(comment);
        }
        jdbcTemplate.update("DELETE FROM post_likes WHERE post_id=?", new Object[]{post.getPostID()});
        jdbcTemplate.update("DELETE FROM post_tags WHERE post_id=?", new Object[]{post.getPostID()});
        jdbcTemplate.update("DELETE FROM posts WHERE post_id=?", new Object[]{post.getPostID()});
        return post.getPostID();
    }

    public int votePost(long userId, int postId, Boolean vote) {

        if (jdbcTemplate.queryForObject("SELECT COUNT(*) " +
                        "FROM post_likes WHERE profile_id=? AND post_id=?",
                new Object[]{userId, postId}, Integer.class) == 1) {

            return jdbcTemplate.update("UPDATE post_likes SET status=? WHERE user_ID=? AND post_id=?",
                    new Object[]{vote, userId, postId});
        } else {
            return jdbcTemplate.update("INSERT INTO post_likes(post_id,profile_id,status) " +
                    "VALUES(?,?,?)", new Object[]{postId, userId, vote});
        }
    }

    private String OrderByMethod(String orderd) {
        String sql = "";
        switch (orderd) {      // Default case is like 'hot' but it looked so empty with only one case
            case "hot":        // and it was added like this to show that if we want we can extand easy serach options
                sql = "ORDER BY votes DESC";
                break;
            case "fresh":
                sql = "ORDER BY date_time_created DESC";
                break;
            default:
                sql = "ORDER BY votes DESC";
        }
        return sql;
    }

    private ResponsePostDTO mapRowBasicDTO(ResultSet rs) throws SQLException {
        return new ResponsePostDTO(rs.getInt("post_ID"), rs.getInt("user_ID"),
                rs.getString("title"), rs.getString("content_URL"),
                rs.getInt("section_ID"), rs.getString("date_time_created"),
                rs.getBoolean("see_sensitive"), rs.getBoolean("attribute_poster"),
                rs.getInt("comments"), rs.getInt("votes"));
    }

    private Post mapRow(ResultSet rs) throws SQLException {
        return new Post(rs.getInt("post_ID"), rs.getInt("user_ID"),
                rs.getString("title"), rs.getString("content_URL"),
                rs.getInt("section_ID"), rs.getString("date_time_created"),
                rs.getBoolean("see_sensitive"), rs.getBoolean("attribute_poster"));
    }
}

package ittalents_final_project.ninegag.Models.DAO;

import ittalents_final_project.ninegag.Models.DTO.ResponsePostDTO;
import ittalents_final_project.ninegag.Models.POJO.Comment;
import ittalents_final_project.ninegag.Models.POJO.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PostDAO {

    public static final String SQL = "SELECT  post_ID, title, content_URL, profile_ID, s.section_ID, " +
            "date_time_created, seeSensitive, attribute_poster, (SELECT COUNT(*)" +
            " FROM comments  WHERE post_ID= ?)AS comments, " +
            "(SELECT COUNT(*) FROM post_likes WHERE post_id= ? AND status=1" +
            " -(SELECT COUNT(*) FROM post_likes WHERE post_id= ? AND status=0))AS votes " +
            "FROM posts p JOIN sections s ON (p.section_ID=s.section_ID)";
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CommentDAO commentDAO;

    public Post getPostById(int Id) {
        try {
            String sql = "SELECT post_ID, title, content_URL, profile_ID, section_ID, " +
                    "date_time_created, seeSensitive, attribute_poster FROM posts WHERE post_ID=?";
            return jdbcTemplate.queryForObject(sql, new Object[]{Id}, ((resultSet, i) -> mapRow(resultSet)));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public ResponsePostDTO getBPostDTO(int Id, boolean showComments) {
        try {
            String sql = SQL + " WHERE post_ID=?";

            ResponsePostDTO post = jdbcTemplate.queryForObject(sql, new Object[]{Id, Id, Id, Id},
                    ((resultSet, i) -> mapRowBasicDTO(resultSet)));
            if (showComments) {
                post.setAllComments(commentDAO.getAllByPostDTO(post.getPostID()));
            }
            return post;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<ResponsePostDTO> getAllPostsByUser(int userId) {
        try {
            String sql = SQL + "WHERE profile_ID=? ORDER BY date_time_created DESC ";
            List<ResponsePostDTO> posts = jdbcTemplate.query(sql, new Object[]{userId}, (resultSet, i) -> mapRowBasicDTO(resultSet));
            return posts;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<ResponsePostDTO> getAllPostsByTag(int tag) {
        String sql = "SELECT post_id FROM post_tags WHERE tag_id=? ORDER BY date_time_created DESC";
        List<ResponsePostDTO> posts = new ArrayList<>();
        List<Integer> postsId = jdbcTemplate.queryForList(sql, new Object[]{tag}, Integer.class);
        for (Integer i : postsId) {
            posts.add(this.getBPostDTO(i, false));
        }
        if (posts.size() > 0) {
            return posts;
        } else {
            return null;
        }
    }

    public List<ResponsePostDTO> getAllPostsBySection(int sectionId) {
        String sql = SQL + " WHERE p.section_ID=? ORDER BY date_time_created DESC";
        List<ResponsePostDTO> posts = jdbcTemplate.query(sql,
                new Object[]{sectionId, sectionId, sectionId, sectionId}, (resultSet, i) -> mapRowBasicDTO(resultSet));
        if (posts.size() > 0) {
            return posts;
        } else {
            return null;
        }
    }

    public List<ResponsePostDTO> getAllPostsCommentedBy(int userId) {
        try {
            String sql = "SELECT post_ID FROM comments WHERE profile_ID=? ORDER BY date_time_created DESC";
            List<ResponsePostDTO> posts = new ArrayList<>();
            List<Integer> postsId = jdbcTemplate.queryForList(sql, new Object[]{userId}, Integer.class);
            for (Integer i : postsId) {
                posts.add(this.getBPostDTO(i, false));
            }
            if (posts.size() > 0) {
                return posts;
            } else {
                return null;
            }
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<ResponsePostDTO> getAllPostsVotedBy(int userId) {
        try {
            String sql = "SELECT post_ID FROM post_likes WHERE profile_ID=? ORDER BY date_time_created DESC";
            List<ResponsePostDTO> posts = new ArrayList<>();
            List<Integer> postsId = jdbcTemplate.queryForList(sql, new Object[]{userId}, Integer.class);
            for (Integer i : postsId) {
                posts.add(this.getBPostDTO(i, false));
            }
            return posts;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int addPost(Post post) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO posts(title,content_URL,profile_ID,section_ID,seeSensitive,attribute_poster)" +
                "VALUES(?,?,?,?,?,?)";
        jdbcTemplate.update(sql, new Object[]{post.getTitle(), post.getContentURL(), post.getProfileID(),
                post.getSectionID(), post.isSeeSensitive(), post.isAtrributePoster()});
        return (int) keyHolder.getKey();
    }

    @Transactional
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

            return jdbcTemplate.update("UPDATE post_likes SET status=? WHERE profile_id=? AND post_id=?",
                    new Object[]{vote, userId, postId});
        } else {
            return jdbcTemplate.update("INSERT INTO post_likes(post_id,profile_id,status) " +
                    "VALUES(?,?,?)", new Object[]{postId, userId, vote});
        }
    }

    private ResponsePostDTO mapRowBasicDTO(ResultSet rs) throws SQLException {
        return new ResponsePostDTO(rs.getInt("post_ID"), rs.getInt("profile_ID"),
                rs.getString("title"), rs.getString("content_URL"),
                rs.getInt("section_ID"), rs.getString("date_time_created"),
                rs.getBoolean("seeSensitive"), rs.getBoolean("attribute_poster"),
                rs.getInt("comments"), rs.getInt("votes"));
    }

    private Post mapRow(ResultSet rs) throws SQLException {
        return new Post(rs.getInt("post_ID"), rs.getInt("profile_ID"),
                rs.getString("title"), rs.getString("content_URL"),
                rs.getInt("section_ID"), rs.getString("date_time_created"),
                rs.getBoolean("seeSensitive"), rs.getBoolean("attribute_poster"));
    }
}

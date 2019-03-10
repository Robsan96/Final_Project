package ittalents_final_project.ninegag.Models.DAO;

import ittalents_final_project.ninegag.Models.DTO.ResponseCommentDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ittalents_final_project.ninegag.Models.POJO.Comment;
import ittalents_final_project.ninegag.Models.POJO.Post;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Component
public class CommentDAO {

    static Logger log = Logger.getLogger(CommentDAO.class.getName());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SELECT_COMMENT = "SELECT c.comment_ID , c.content,c.post_ID,c.profile_ID," +
            "c.reply_of_ID,c.date_time_created,(SELECT COUNT(*) FROM comments_likes WHERE comment_id=c.comment_ID " +
            "AND status=1 -(SELECT COUNT(*) FROM comments_likes WHERE comment_id=c.comment_ID AND status=0)) AS votes" +
            ",(SELECT COUNT(*) FROM comments c WHERE reply_of_ID=c.comment_ID)AS replies" +
            ",u.username AS ownerName,u.avatar AS ownerAvatar FROM comments c JOIN users u ON(c.profile_ID=u.user_ID)";

    public int addComment(Comment comment) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO comments(post_ID,profile_ID,content) VALUES(?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, comment.getPost());
            ps.setInt(2, comment.getProfile());
            ps.setString(3, comment.getContent());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public int addReply(Comment comment) {
        String sql = "INSERT INTO comments(post_ID,profile_ID,content,reply_of_ID) VALUES(?,?,?,?)";
        return jdbcTemplate.update(sql, new Object[]{comment.getPost(), comment.getProfile(),
                comment.getContent(), comment.getReply()});
    }

    public ResponseCommentDTO getById(int id) {
        try {
            String sql = SELECT_COMMENT + "WHERE comment_ID=?";

            ResponseCommentDTO comment = jdbcTemplate.queryForObject(sql, new Object[]{id},
                    ((resultSet, i) -> mapRowR(resultSet)));

            return comment;

        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteComment(Comment comment) {
        List<Comment> coments = this.getAllByComment(comment);
        for (Comment coment1 : coments) {
            deleteComment(coment1);
        }
        jdbcTemplate.update("DELETE FROM comments_likes WHERE comment_id=?", new Object[]{comment.getId()});
        jdbcTemplate.update("DELETE FROM comments WHERE reply_of_ID=?", new Object[]{comment.getId()});
        jdbcTemplate.update("DELETE FROM comments WHERE comment_ID=?", new Object[]{comment.getId()});
        return comment.getId();
    }

    public List<ResponseCommentDTO> getAllByPostDTO(int postId) {
        String sql = SELECT_COMMENT + "WHERE post_ID=? AND reply_of_ID IS NULL ORDER BY votes DESC";
        List<ResponseCommentDTO> comments = jdbcTemplate.query(sql, new Object[]{postId},
                (resultSet, i) -> mapRowR(resultSet));
        return comments;
    }

    public List<Comment> getAllByPost(Post post) {

        String sql = "SELECT comment_ID FROM comments WHERE post_ID=? ";
        List<Comment> comments = jdbcTemplate.query(sql, new Object[]{post.getPostID()},
                (resultSet, i) -> mapRow(resultSet));
        return comments;
    } // Using this method for deleting !

    public List<ResponseCommentDTO> getAllByCommentDTO(int commentId) {
        String sql = SELECT_COMMENT + "WHERE reply_of_ID=? ORDER BY date_time_created DESC";
        List<ResponseCommentDTO> comments = jdbcTemplate.query(sql, new Object[]{commentId}, (resultSet, i) -> mapRowR(resultSet));
        if (comments.size() > 0) {
            return comments;
        } else {
            return null;
        }
    }

    public List<ResponseCommentDTO> getAllFreshByPostDTO(int postId) {
        String sql = SELECT_COMMENT + "WHERE post_ID=? AND reply_of_ID IS NULL ORDER BY date_time_created DESC";
        List<ResponseCommentDTO> comments = jdbcTemplate.query(sql, new Object[]{postId},
                (resultSet, i) -> mapRowR(resultSet));
        return comments;
    }

    private List<Comment> getAllByComment(Comment comment) {
        String sql = "SELECT * FROM comments WHERE reply_of_ID=? ORDER BY date_time_created DESC";
        List<Comment> comments = jdbcTemplate.query(sql, new Object[]{comment.getId()}, (resultSet, i) -> mapRow(resultSet));
        return comments;
    }

    public int voteComment(long userId, int commentId, boolean vote) {
        if (jdbcTemplate.queryForObject("SELECT COUNT(*) " +
                        "FROM comments_likes WHERE profile_id=? AND comment_id=?",
                new Object[]{userId, commentId}, Integer.class) == 1) {

            return jdbcTemplate.update("UPDATE comments_likes SET status=? WHERE profile_id=? AND comment_id=?",
                    new Object[]{vote, userId, commentId});
        } else {
            return jdbcTemplate.update("INSERT INTO comments_likes(comment_id,profile_id,status) " +
                    "VALUES(?,?,?)", new Object[]{commentId, userId, vote});
        }
    }

    public int uppdateComment(Comment comment) {
        String sql = "UPDATE comments " +
                "SET content=?" +
                "WHERE comment_ID=?";
        return jdbcTemplate.update(sql, new Object[]{comment.getContent(), comment.getId()});
    }

    private Comment mapRow(ResultSet rs) throws SQLException {
        return new Comment(rs.getInt("comment_ID"),
                rs.getString("content"),
                rs.getInt("post_ID"),
                rs.getInt("profile_ID"),
                rs.getInt("reply_of_ID"),
                rs.getString("date_time_created"));
    }

    private ResponseCommentDTO mapRowR(ResultSet rs) throws SQLException {
        return new ResponseCommentDTO(rs.getInt("comment_ID"),
                rs.getString("content"),
                rs.getInt("post_ID"),
                rs.getInt("profile_ID"),
                rs.getInt("reply_of_ID"),
                rs.getString("date_time_created"),
                rs.getInt("votes"),
                rs.getInt("replies"),
                rs.getString("ownerName"),
                rs.getString("ownerAvatar"));
    }
}
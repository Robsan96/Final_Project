package ittalents_final_project.ninegag.Models.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ittalents_final_project.ninegag.Models.POJO.Comment;
import ittalents_final_project.ninegag.Models.POJO.Post;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class CommentDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;
    private static final String SELECT_COMMENT = "SELECT comment_ID , content,post_ID,profile_ID,reply_of_ID,date_time_created " +
            "FROM comments ";

    public int addComment(Comment comment) {
        String sql = "INSERT INTO comments(post_ID,profile_ID,content) VALUES(?,?,?)";
        return jdbcTemplate.update(sql, new Object[]{comment.getPost(), comment.getProfile(), comment.getContent()});
    }

    public Comment getById(int id) {
        try {
            String sql = SELECT_COMMENT + "WHERE comment_ID=?";

            Comment comment = jdbcTemplate.queryForObject(sql, new Object[]{id}, ((resultSet, i) -> mapRow(resultSet)));

            return comment;

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int deleteComment(Comment comment) {
        String sql = "DELETE FROM comments WHERE comment_ID=?";
        return jdbcTemplate.update(sql, new Object[]{comment.getId()});
    }

    public List<Comment> getAllByPost(Post post) {
        try {
            String sql = SELECT_COMMENT + "WHERE post_ID=?";
            List<Comment> comments = jdbcTemplate.query(sql, new Object[]{post.getPostID()}, (resultSet, i) -> mapRow(resultSet));
            return comments;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Comment> getAllByComment(Comment comment) {
        String sql = SELECT_COMMENT + "WHERE reply_of_ID=?";
        List<Comment> comments = jdbcTemplate.query(sql, new Object[]{comment.getId()}, (resultSet, i) -> mapRow(resultSet));
        return comments;
    }

    public int voteComment(int userId, int commentId, boolean vote) {
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

    public int getAllVotes(int comentId) {
        String sql = "SELECT COUNT(*)-(SELECT COUNT(*)FROM comments_likes WHERE comment_ID=? AND status=0)AS points " +
                "FROM comments_likes WHERE comment_ID=? AND status=1";
        int i = jdbcTemplate.queryForObject(sql, new Object[]{comentId, comentId}, Integer.class);
        return i;
    }

    private Comment mapRow(ResultSet rs) throws SQLException {
        return new Comment(rs.getInt("comment_ID"),
                rs.getString("content"),
                rs.getInt("post_ID"),
                rs.getInt("profile_ID"),
                rs.getInt("reply_of_ID"),
                rs.getString("date_time_created"));
    }
}
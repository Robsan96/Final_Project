package ittalents_final_project.ninegag.Models.DAO;

import ittalents_final_project.ninegag.Models.DTO.ResponsePostDTO;
import ittalents_final_project.ninegag.Models.POJO.Comment;
import ittalents_final_project.ninegag.Models.POJO.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PostDAO {

    public static final String SQL = "SELECT  post_ID, title, content_URL, profile_ID, section_ID, date_time_created, seeSensitive, attribute_poster, (SELECT COUNT(*) FROM comments WHERE post_ID= ?)AS comments, (SELECT COUNT(*) FROM post_likes WHERE post_id= ? AND status=1 -(SELECT COUNT(*) FROM post_likes WHERE post_id= ? AND status=0))AS votes FROM posts";
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

    public ResponsePostDTO getBPostDTO(int Id) {
        try {
            String sql = SQL + " WHERE post_ID=?";

            ResponsePostDTO post= jdbcTemplate.queryForObject(sql, new Object[]{Id,Id,Id,Id}, ((resultSet, i) -> mapRowBasicDTO(resultSet)));
            post.setAllComments(commentDAO.getAllByPost(post));
            return post;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Post> getAllPostsByUser(int userId) {
        try {
            String sql = SQL + "WHERE profile_ID=?";
            List<Post> posts = jdbcTemplate.query(sql, new Object[]{userId}, (resultSet, i) -> mapRow(resultSet));
            return posts;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<ResponsePostDTO> getAllPostsByTag(int tag) {
        try {
            String sql = "SELECT post_id FROM post_tags WHERE tag_id=?";
            List<ResponsePostDTO> posts = new ArrayList<>();
            List<Integer> postsId = jdbcTemplate.queryForList(sql, new Object[]{tag}, Integer.class);
            for (Integer i : postsId) {
                posts.add(this.getBPostDTO(i));
            }
            return posts;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<ResponsePostDTO> getAllPostsBySection(int sectionId) {
        try {
            String sql = SQL + " WHERE section_ID=?";
            List<ResponsePostDTO> posts = jdbcTemplate.query(sql,
                    new Object[]{sectionId, sectionId, sectionId, sectionId}, (resultSet, i) -> mapRowBasicDTO(resultSet));
            return posts;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<ResponsePostDTO> getAllPostsCommentedBy(int userId) {
        try {
            String sql = "SELECT post_ID FROM comments WHERE profile_ID=?";
            List<ResponsePostDTO> posts = new ArrayList<>();
            List<Integer> postsId = jdbcTemplate.queryForList(sql, new Object[]{userId}, Integer.class);
            for (Integer i : postsId) {
                posts.add(this.getBPostDTO(i));
            }
            return posts;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<ResponsePostDTO> getAllPostsVotedBy(int userId) {
        try {
            String sql = "SELECT post_ID FROM post_likes WHERE profile_ID=?";
            List<ResponsePostDTO> posts = new ArrayList<>();
            List<Integer> postsId = jdbcTemplate.queryForList(sql, new Object[]{userId}, Integer.class);
            for (Integer i : postsId) {
                posts.add(this.getBPostDTO(i));
            }
            return posts;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int addPost(Post post) {
        String sql = "INSERT INTO posts(title,content_URL,profile_ID,section_ID,seeSensitive,attribute_poster)" +
                "VALUES(?,?,?,?,?,?)";
        return jdbcTemplate.update(sql, new Object[]{post.getTitle(), post.getContentURL(), post.getProfileID(),
                post.getSectionID(), post.isSeeSensitive(), post.isAtrributePoster()});
    }

    public int removePost(Post post) {
        try {
            List<Comment> comments = commentDAO.getAllByPost(post);
            for (Comment comment : comments) {
                commentDAO.deleteComment(comment);
            }
            jdbcTemplate.update("DELETE FROM post_likes WHERE post_id=?", new Object[]{post.getPostID()});
            jdbcTemplate.update("DELETE FROM posts WHERE post_id=?", new Object[]{post.getPostID()});
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public int votePost(int userId,int postId,Boolean vote){

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

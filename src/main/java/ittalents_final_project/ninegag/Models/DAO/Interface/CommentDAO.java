package ittalents_final_project.ninegag.Models.DAO.Interface;

import ittalents_final_project.ninegag.Models.DTO.ResponseCommentDTO;
import ittalents_final_project.ninegag.Models.POJO.Comment;
import ittalents_final_project.ninegag.Models.POJO.Post;

import java.util.List;

public interface CommentDAO {

    int addComment(Comment comment);

    int addReply(Comment comment);

    int deleteComment(Comment comment);

    ResponseCommentDTO getById(int id);

    List<Comment> getAllByPost(Post post);

    List <ResponseCommentDTO> getAllByCommentDTO(int commentId);

    List<ResponseCommentDTO> getAllFreshByPostDTO(int postId);

    List<Comment> getAllByComment(Comment comment);

    int voteComment(long userId, int commentId, boolean vote);

    int uppdateComment(Comment comment);



}

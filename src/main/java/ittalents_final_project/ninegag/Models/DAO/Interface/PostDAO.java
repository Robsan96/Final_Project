package ittalents_final_project.ninegag.Models.DAO.Interface;

import ittalents_final_project.ninegag.Models.DTO.ResponsePostDTO;
import ittalents_final_project.ninegag.Models.POJO.Post;

import java.util.List;

public interface PostDAO {

    Post getPostById(int Id);

    List<ResponsePostDTO> getAllPostsBy(String orderd);

    ResponsePostDTO getBPostDTO(int Id, boolean showComments);

    List<ResponsePostDTO> getAllPostsByUser(int userId);

    List<ResponsePostDTO> getAllPostsByTag(int tag, String orderd);

    List<ResponsePostDTO> getAllPostsBySection(int sectionId, String orderd);

    List<ResponsePostDTO> getAllPostsCommentedBy(int userId);

    List<ResponsePostDTO> getAllPostsMadeBy(int userId);

    List<ResponsePostDTO> getAllPostsVotedBy(int userId);

    int addPost(Post post);

    int removePost(Post post);

    int votePost(long userId, int postId, Boolean vote);
}

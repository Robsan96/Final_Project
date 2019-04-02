package ittalents_final_project.ninegag.Models.DAO.Interface;

import ittalents_final_project.ninegag.Models.POJO.Tag;

import java.util.List;

public interface TagDAO {

    Tag getByName(String name);

    Tag getById(int tagId);

    List<Tag> getTagsByPost(int postId);

    void setTags(int postId, List<Tag> tags);

    int addTag(String tagName);


}

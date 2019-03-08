package ittalents_final_project.ninegag.Models.DTO;

import ittalents_final_project.ninegag.Models.POJO.Comment;
import ittalents_final_project.ninegag.Models.POJO.Post;
import ittalents_final_project.ninegag.Models.POJO.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Component
public class RequestPostDTO extends Post {

    private List<Tag> tags = new ArrayList<>(3);

    public RequestPostDTO(int postID, int profileID, String title, String contentURL,
                          int sectionID, String creationDate, boolean seeSensitive, boolean atrributePoster,
                          List<Tag> tagList) {
        super(postID, profileID, title, contentURL, sectionID, creationDate, seeSensitive, atrributePoster);
        this.tags = tagList;
    }
}

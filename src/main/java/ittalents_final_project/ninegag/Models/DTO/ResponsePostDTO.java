package ittalents_final_project.ninegag.Models.DTO;

import ittalents_final_project.ninegag.Models.POJO.Comment;
import ittalents_final_project.ninegag.Models.POJO.Post;
import javafx.geometry.Pos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePostDTO extends Post {

    private int commentsCounter;
    private int votePoints;
    private List<Comment> allComments;

    public ResponsePostDTO(int postID, int profileID, String title, String contentURL, int sectionID,
                           String creationDate, boolean seeSensitive, boolean atrributePoster,
                           int commentsCounter, int votePoints) {
        super(postID, profileID, title, contentURL, sectionID, creationDate, seeSensitive, atrributePoster);
        this.commentsCounter = commentsCounter;
        this.votePoints = votePoints;
        this.allComments = new ArrayList<>();
    }
}

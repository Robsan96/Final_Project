package ittalents_final_project.ninegag.Models.DTO;

import ittalents_final_project.ninegag.Models.POJO.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class ResponseCommentDTO extends Comment {

    private int votes;
    private int replies;
    private String ownerName;
    private String ownerAvatar;

    public ResponseCommentDTO(int id, String content, int post, int profile, int reply, String creationDate,
                              int votes,int replies, String ownerName, String ownerAvatar) {
        super(id, content, post, profile, reply, creationDate);
        this.votes = votes;
        this.replies=replies;
        this.ownerName = ownerName;
        this.ownerAvatar = ownerAvatar;
    }
}

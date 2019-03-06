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

    private String ownerName;
    private String owenrAvatar;

    public ResponseCommentDTO(int id, String content, int post, int profile, int reply, String creationDate,
                              String ownerName, String owenrAvatar) {
        super(id, content, post, profile, reply, creationDate);
        this.ownerName = ownerName;
        this.owenrAvatar = owenrAvatar;
    }
}

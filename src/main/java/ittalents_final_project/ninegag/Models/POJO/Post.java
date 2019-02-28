package ittalents_final_project.ninegag.Models.POJO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Post {

    private String title;
    private String contentURL;
    private int profileID;
    private int postID;
    private String creationDate;
    private boolean seeSensitive;
    private boolean atrributePoster;


}

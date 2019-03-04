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

    private int postID;
    private int profileID;
    private String title;
    private String contentURL;
    private int sectionID;
    private String creationDate;
    private boolean seeSensitive;
    private boolean atrributePoster;


}

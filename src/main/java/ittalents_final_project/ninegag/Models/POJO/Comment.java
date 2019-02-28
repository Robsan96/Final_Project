package ittalents_final_project.ninegag.Models.POJO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private int id;
    private String content;
    private int post;
    private int profile;
    private int reply;
    private String creationDate;
}


package ittalents_final_project.ninegag.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserDTO {

    public UserDTO(String email) {
        this.email = email;
    }

    private int user_ID;
    private String email;
    private String username;
    private String full_name;
    private java.sql.Date date_created;
    private java.sql.Date birthday;
    private int gender_ID;
    private int country_ID;
    private String description;
    private String facebook_account;
    private String google_account;
    private String avatar;
    private boolean sensitive_filter;
}

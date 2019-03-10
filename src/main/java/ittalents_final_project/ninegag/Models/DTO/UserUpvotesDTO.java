package ittalents_final_project.ninegag.Models.DTO;

import ittalents_final_project.ninegag.Models.POJO.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
@NoArgsConstructor
public class UserUpvotesDTO extends User {

    private List<ResponsePostDTO> likedPosts;

    public UserUpvotesDTO(int user_ID) {
        super(user_ID);
        this.likedPosts = new ArrayList<>();
    }

    public UserUpvotesDTO(int user_ID, String email, String username, String full_name, Date date_created,
                        Date birthday, int gender_ID, int country_ID, String description, String facebook_account, String google_account,
                        String avatar, boolean sensitive_filter, boolean admin_privileges) {
        super(user_ID, email, username, full_name, date_created, birthday, gender_ID, country_ID, description, facebook_account, google_account, avatar, sensitive_filter, admin_privileges);
        this.likedPosts = new ArrayList<>();
    }




}

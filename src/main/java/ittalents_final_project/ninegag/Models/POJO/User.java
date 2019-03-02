package ittalents_final_project.ninegag.Models.POJO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private long user_ID;
    private String email;
    private String username;
    private String password;
    private String salt;
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
    private boolean admin_privileges;


    public User(long user_ID) {
        this.user_ID = user_ID;
    }

    public User(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_ID=" + user_ID +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", full_name='" + full_name + '\'' +
                ", date_created=" + date_created +
                ", birthday=" + birthday +
                ", gender_ID=" + gender_ID +
                ", country_ID=" + country_ID +
                ", description='" + description + '\'' +
                ", facebook_account='" + facebook_account + '\'' +
                ", google_account='" + google_account + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sensitive_filter=" + sensitive_filter +
                ", admin_privileges=" + admin_privileges +
                '}';
    }
}

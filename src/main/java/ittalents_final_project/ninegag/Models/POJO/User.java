package ittalents_final_project.ninegag.Models.POJO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private int user_ID;
    private String email;
    private String username;
    private String password;
    private String newPassword;
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


    public User(int user_ID) {
        this.user_ID = user_ID;
    }

    public User(String email) {
        this.email = email;
    }

    public User(int user_ID, String password) {
        this.user_ID = user_ID;
        this.password = password;
    }

    public User(int user_ID, String email, String username, String full_name, Date date_created, Date birthday, int gender_ID,
                int country_ID, String description, String facebook_account, String google_account, String avatar,
                boolean sensitive_filter, boolean admin_privileges) {
        this.user_ID = user_ID;
        this.email = email;
        this.username = username;
        this.full_name = full_name;
        this.date_created = date_created;
        this.birthday = birthday;
        this.gender_ID = gender_ID;
        this.country_ID = country_ID;
        this.description = description;
        this.facebook_account = facebook_account;
        this.google_account = google_account;
        this.avatar = avatar;
        this.sensitive_filter = sensitive_filter;
        this.admin_privileges = admin_privileges;
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

package ittalents_final_project.ninegag.Models.POJO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Gender {

    private int gender_ID;
    private String gender_Type;

    public Gender(int gender_ID) {
        this.gender_ID = gender_ID;
    }

    public Gender(String gender_Type) {
        this.gender_Type = gender_Type;
    }

    @Override
    public String toString() {
        return "Gender{" +
                "gender_ID=" + gender_ID +
                ", gender_Type='" + gender_Type + '\'' +
                '}';
    }
}

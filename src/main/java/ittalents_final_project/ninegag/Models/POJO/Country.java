package ittalents_final_project.ninegag.Models.POJO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Country {

    int country_ID;
    String country_name;

    public Country(String country_name) {
        this.country_name = country_name;
    }

    public Country(int country_ID) {
        this.country_ID = country_ID;
    }

    @Override
    public String toString() {
        return "Country{" +
                "country_ID=" + country_ID +
                ", country_name='" + country_name + '\'' +
                '}';
    }
}

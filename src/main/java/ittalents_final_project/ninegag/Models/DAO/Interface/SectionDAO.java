package ittalents_final_project.ninegag.Models.DAO.Interface;

import ittalents_final_project.ninegag.Models.POJO.Section;

import java.sql.ResultSet;
import java.util.List;

public interface SectionDAO {

    List<Section> getAll();

    Section getByName(String section_name);

    Section getById(int id);

    int addSection(String name);

    int deleteSection(Section section);
}

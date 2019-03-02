package ittalents_final_project.ninegag.Controllers;

import ittalents_final_project.ninegag.Models.DAO.SectionDAO;
import ittalents_final_project.ninegag.Models.POJO.Section;
import ittalents_final_project.ninegag.Utils.Exceptions.AlreadyExistsException;
import ittalents_final_project.ninegag.Utils.Exceptions.NotLoggedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/sections")
public class SectionController extends BaseController {

    @Autowired
    SectionDAO dao;


    @GetMapping(value = "/")
    public List<Section> showAllSections() {
        List<Section> sections = dao.getAll();
        if (sections == null) {
            throw new NullPointerException();
        } else {
            return sections;
        }
    }

    @GetMapping(value = "/id/{value}")
    public Section showSection(@PathVariable(value = "value") int id) throws NotLoggedException {
        Section section = dao.getById(id);
        if (section == null) {
            throw new NullPointerException("Section with that id does not exist");
        } else {
            return section;
        }
    }

    @GetMapping(value = "/{name}")
    public Section showSection(@PathVariable(value = "name") String name) {
        Section section = dao.getByName(name);
        if (section == null) {
            throw new NullPointerException("Section with that name does not exist");
        } else {
            return section;
        }
    }

    @PostMapping(value = "/{name}")
    public String addSection(@PathVariable(value = "name") String name) throws AlreadyExistsException {
        if (dao.getByName(name) == null) {
            if (dao.addSection(name) == 1) {
                return "Section added successfully";
            } else {
                return "Section was not added for some reason,pls try again or contact the programer";
            }
        } else {
            throw new AlreadyExistsException("This already exist !");
        }
    }
}

package ittalents_final_project.ninegag.Controllers;

import ittalents_final_project.ninegag.Models.DAO.SectionDAO;
import ittalents_final_project.ninegag.Models.POJO.Section;
import ittalents_final_project.ninegag.Utils.Exceptions.AlreadyExistsException;
import ittalents_final_project.ninegag.Utils.Exceptions.NotLoggedException;
import ittalents_final_project.ninegag.Utils.Exceptions.PermitionDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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
    public Section showSection(@PathVariable(value = "value") int id, HttpSession session) throws NotLoggedException, PermitionDeniedException {
        if (validateAdmin(session)) {
            Section section = dao.getById(id);
            if (section == null) {
                throw new NullPointerException("Section with that id does not exist");
            } else {
                return section;
            }
        } else {
            throw new PermitionDeniedException("You dont have access to that option");
        }
    }

    @GetMapping(value = "/{name}")
    public Section showSection(@PathVariable(value = "name") String name, HttpSession session) throws NotLoggedException, PermitionDeniedException {
        if (validateAdmin(session)) {
            Section section = dao.getByName(name);
            if (section == null) {
                throw new NullPointerException("Section with that name does not exist");
            } else {
                return section;
            }
        } else {
            throw new PermitionDeniedException("You dont have access to that option");
        }
    }

    @PostMapping(value = "/{name}")
    public String addSection(@PathVariable(value = "name") String name, HttpSession session) throws AlreadyExistsException, NotLoggedException, PermitionDeniedException {
        if (validateAdmin(session)) {
            if (dao.getByName(name) == null) {
                if (dao.addSection(name) == 1) {
                    return "Section added successfully";
                } else {
                    return "Section was not added for some reason,pls try again or contact the programer";
                }
            } else {
                throw new AlreadyExistsException("This already exist !");
            }
        } else {
            throw new PermitionDeniedException("You dont have acces to that option");
        }
    }
}

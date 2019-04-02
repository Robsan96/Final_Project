package ittalents_final_project.ninegag.Controllers;

import ittalents_final_project.ninegag.Models.DAO.Implement.SectionDAOimpl;
import ittalents_final_project.ninegag.Models.POJO.Section;
import ittalents_final_project.ninegag.Utils.Exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;


@RestController
@RequestMapping(value = "/sections")
public class SectionController extends BaseController {

    @Autowired
    SectionDAOimpl dao;


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
    public Section showSection(@PathVariable(value = "value") int id, HttpSession session)
            throws NotLoggedException, NotAdminException, BadParamException {
        if (validateAdmin(session)) {
            Section section = dao.getById(id);
            if (section == null) {
                throw new BadParamException("Section with that id does not exist");
            } else {
                return section;
            }
        } else {
            throw new NotAdminException();

        }
    }

    @GetMapping(value = "/{name}")
    public Section showSection(@PathVariable(value = "name") String name, HttpSession session)
            throws NotLoggedException, NotAdminException, BadParamException {
        if (validateAdmin(session)) {
            Section section = dao.getByName(name);
            if (section == null) {
                throw new BadParamException("Section with that name does not exist");
            } else {
                return section;
            }
        } else {
            throw new NotAdminException();
        }
    }

    @PostMapping(value = "/add")
    public String addSection(@RequestBody Section section, HttpSession session)
            throws AlreadyExistsException, NotLoggedException, PermitionDeniedException, BadParamException {
        if (validateAdmin(session)) {
            if (section.getName().isEmpty() || section.getName() == null) {
                throw new BadParamException("Name cant be empty");
            }
            if (dao.getByName(section.getName()) == null) {
                return "Section was added with ID -> " + dao.addSection(section.getName());
            } else {
                throw new AlreadyExistsException("This section already exist !");
            }
        } else {
            throw new PermitionDeniedException("You dont have access to that option");
        }
    }

    @DeleteMapping(value = "/delete")
    public String deleteSection(@RequestBody Section section, HttpSession session)
            throws NotAdminException, NotLoggedException, BadParamException {
        if (!validateAdmin(session)) {
            throw new NotAdminException();
        }
        if (dao.getById(section.getId()) == null) {
            throw new BadParamException("Section with that id does not exist!");
        } else {
            return "Section with ID " + dao.deleteSection(section) + " deleted";
        }
    }
}

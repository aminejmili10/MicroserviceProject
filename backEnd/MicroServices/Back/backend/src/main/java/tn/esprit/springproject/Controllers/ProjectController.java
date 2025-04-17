package tn.esprit.springproject.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.springproject.Entities.Project;
import tn.esprit.springproject.Services.ProjectService;

import java.util.List;

@RestController
@RequestMapping("/project")
@AllArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping("/getAllProjects")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }
}
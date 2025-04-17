package tn.esprit.springproject.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tn.esprit.springproject.Entities.Project;
import tn.esprit.springproject.Services.IResourceSerivce;

@Aspect
@Component
@Slf4j
public class ProjectAspect {
    @Autowired
    private IResourceSerivce resourceService;

    // Trigger after addProject is successfully executed
    @AfterReturning(pointcut = "execution(* tn.esprit.springproject.Services.IResourceSerivce.addProject(..))", returning = "result")
    public void afterProjectAdded(Object result) {
        Project project = (Project) result;
        log.info("Project added successfully: {}", project.getName());}
}

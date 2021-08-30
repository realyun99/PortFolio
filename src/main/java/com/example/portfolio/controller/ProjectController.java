package com.example.portfolio.controller;

import com.example.portfolio.domain.Project;
import com.example.portfolio.exception.DataNotFoundException;
import com.example.portfolio.filter.ProjectFilter;
import com.example.portfolio.service.FileUploadService;
import com.example.portfolio.service.ProjectService;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/project")
//@Secured("ROLE_ADMIN")
public class ProjectController {

  @NonNull
  private final ProjectService projectService;
  @NonNull
  private final FileUploadService fileUploadService;


  @GetMapping("")
  public String list(Model model, Pageable pageable, ProjectFilter filter) {

    model.addAttribute("projectList", projectService.findAllByFilter(pageable, filter));
    model.addAttribute("filter", filter);
    return "project/list";
  }

  @GetMapping({"/edit", "/edit/{id}"})
  public String edit(@PathVariable(required = false) Integer id, Model model) {

    Project project = (id == null) ? new Project() : projectService.findById(id).orElseThrow(
        DataNotFoundException::new);

    model.addAttribute("project", project);

    return "project/edit";
  }

  @GetMapping("/read/{id}")
  public String read(@PathVariable Integer id, Model model) {

    Project project = projectService.findById(id).orElseThrow(DataNotFoundException::new);
    model.addAttribute("project", project);

    return "project/read";
  }

  @PostMapping("/save")
  public String save(@ModelAttribute("project") Project project, BindingResult result,
      MultipartFile[] files) {

    if (!result.hasErrors()) {
      if (files != null) {
        for (MultipartFile file : files) {
          try {
            project.getAttachments().add(fileUploadService.uploadFile(file));
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
      projectService.save(project);
      return "project/list";
    }
    return null;
  }
}

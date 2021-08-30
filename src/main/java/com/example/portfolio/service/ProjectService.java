package com.example.portfolio.service;

import com.example.portfolio.domain.Project;
import com.example.portfolio.dto.ProjectListDTO;
import com.example.portfolio.filter.ProjectFilter;
import com.example.portfolio.repository.ProjectRepository;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService {

  @NonNull
  private final ProjectRepository projectRepository;

  public Optional<Project> findById(Integer id) { return projectRepository.findById(id); }

  public Project save(Project project) {
    return projectRepository.save(project);
  }

  public Page<ProjectListDTO> findAllByFilter(Pageable pageable, ProjectFilter filter) {
    return projectRepository.findAllByFilter(pageable, filter);
  }

  @Transactional
  public int deleteAllByIdIn(List<Integer> ids) {
    return projectRepository.deleteAllByIdIn(ids);
  }

}

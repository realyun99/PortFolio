package com.example.portfolio.repository.custom;

import com.example.portfolio.dto.ProjectListDTO;
import com.example.portfolio.filter.ProjectFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomProjectRepository {

  Page<ProjectListDTO> findAllByFilter(Pageable pageable, ProjectFilter filter);

}

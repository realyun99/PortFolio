package com.example.portfolio.repository.support;

import com.example.portfolio.domain.Project;
import com.example.portfolio.domain.QProject;
import com.example.portfolio.dto.ProjectListDTO;
import com.example.portfolio.dto.QProjectListDTO;
import com.example.portfolio.filter.ProjectFilter;
import com.example.portfolio.repository.custom.CustomProjectRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ProjectRepositoryImpl extends QuerydslRepositorySupport implements
    CustomProjectRepository {

  private final QProject project = QProject.project;

  public ProjectRepositoryImpl() {
    super(Project.class);
  }

  @Override
  public Page<ProjectListDTO> findAllByFilter(Pageable pageable, ProjectFilter filter) {
    BooleanBuilder builder = new BooleanBuilder();

    if (filter.getFStartDateTime() != null) {
      builder.and(project.createDate.goe(filter.getFStartDateTime()));
    }

    if (filter.getFEndDateTime() != null) {
      builder.and(project.createDate.loe(filter.getFEndDateTime()));
    }
    if (!StringUtils.isBlank(filter.getTitle())) {
      builder.and(project.title.containsIgnoreCase(filter.getTitle()));
    }

    final JPQLQuery<ProjectListDTO> query = from(project).where(builder)
        .select(new QProjectListDTO(project.id, project.date, project.title,
            project.attachments.size()));

    List<ProjectListDTO> result = getQuerydsl().applyPagination(pageable, query).fetch();

    return new PageImpl<>(result, pageable, query.fetchCount());

  }

}

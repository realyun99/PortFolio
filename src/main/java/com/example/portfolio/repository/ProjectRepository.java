package com.example.portfolio.repository;

import com.example.portfolio.domain.Project;
import com.example.portfolio.repository.custom.CustomProjectRepository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer>,
    CustomProjectRepository {

  int deleteAllByIdIn(List<Integer> ids);
}

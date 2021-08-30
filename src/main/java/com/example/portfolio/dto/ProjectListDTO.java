package com.example.portfolio.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectListDTO {

  private Integer id;
  private String date;
  private String title;
  private int attachmentCount;

  @QueryProjection
  public ProjectListDTO(Integer id, String date, String title, int attachmentCount) {
    this.id = id;
    this.date = date;
    this.title = title;
    this.attachmentCount = attachmentCount;
  }

}

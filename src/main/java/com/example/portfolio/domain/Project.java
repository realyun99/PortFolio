package com.example.portfolio.domain;

import com.example.portfolio.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Entity
@Table(name = "project", indexes = {})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Project {
  //기본정보
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Integer id;

  @JsonIgnore
  @DateTimeFormat(pattern = DateUtil.PATTERN_YMDHMS)
  @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "timestamp(6) default current_timestamp(6)")
  private LocalDateTime createDate;   //생성일시

  @JsonIgnore
  @DateTimeFormat(pattern = DateUtil.PATTERN_YMDHMS)
  @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "timestamp(6) default current_timestamp(6) on update current_timestamp(6)")
  private LocalDateTime updateDate;   //변경일시

  //추가정보
  @Column(nullable = false, length = 255)
  private String date; //프로젝트 진행날짜

  @Column(nullable = false)
  private String title; // 프로젝트 제목

  private String purpose; // 프로젝트 목적

  private String technology; // 프로젝트 사용기술

  private String address; // 프로젝트 깃헙주소

  // 첨부파일
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinTable(name = "attachment_file", joinColumns = @JoinColumn(name = "file_id"), inverseJoinColumns = @JoinColumn(name = "attachment_id"))
  private List<Attachment> attachments = new ArrayList<>();
}

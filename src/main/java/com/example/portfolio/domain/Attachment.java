package com.example.portfolio.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.io.FileUtils;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "attachment")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Attachment {

  public Attachment(Long id) {
    this.id = id;
  }

  public Attachment(String fileName, String filePath, long fileSize, String fileContentType) {
    this.fileName = fileName;
    this.filePath = filePath;
    this.fileSize = fileSize;
    this.fileContentType = fileContentType;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @Column(updatable = false, nullable = false)
  private String fileName; // 원본 파일명

  @Column(updatable = false, nullable = false, unique = true)
  private String filePath; // 실제 저장된 파일명

  @Column(updatable = false)
  private String fileContentType;

  @Column(updatable = false)
  private long fileSize;

  @PreRemove
  public void onRemove() {
    File file = new File(filePath);
    FileUtils.deleteQuietly(file);
  }

  @JsonIgnore
  public String getEncodedFileName() throws UnsupportedEncodingException {
    return URLEncoder.encode(this.fileName, "UTF-8").replace("+", "%20");
  }

  public String   getFileSizeText() {
    float size = fileSize;
    String unit = "Byte";
    if (size >= 1024) {
      unit = "KB";
      size /= 1024;
      if (size >= 1024) {
        unit = "MB";
        size /= 1024;
      }
    }
    return String.format("%d%s", (int) size, unit);
  }

  public String getIconPath() {
    String type = "file";
    if(fileContentType.toLowerCase().startsWith("image")) {
      type = "img";
    } else {
      String lowerCaseName = fileName.toLowerCase();
      if(lowerCaseName.endsWith("doc") || lowerCaseName.endsWith("docx")) {
        type = "doc";
      } else if(lowerCaseName.endsWith("hwp")) {
        type = "hwp";
      } else if(lowerCaseName.endsWith("pdf")) {
        type = "pdf";
      } else if(lowerCaseName.endsWith("xls") || lowerCaseName.endsWith("xlsx")) {
        type = "xl";
      } else if(lowerCaseName.endsWith("zip")) {
        type = "zip";
      } else if(lowerCaseName.endsWith("ppt") || lowerCaseName.endsWith("pptx")) {
        type = "ppt";
      }
    }
    return "static/img/files/" + String.format("icon-%s.png", type);
  }
}

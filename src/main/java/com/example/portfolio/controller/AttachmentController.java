package com.example.portfolio.controller;

import com.example.portfolio.domain.Attachment;
import com.example.portfolio.exception.DataNotFoundException;
import com.example.portfolio.service.AttachmentService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attachment")
@RequiredArgsConstructor
public class AttachmentController {

  @NonNull
  private final AttachmentService attachmentService;

  @GetMapping(value = "/{id}", produces = "application/octet-stream")
  public ResponseEntity attachment(@PathVariable("id") Long id) throws IOException {
    Attachment attachment = attachmentService.findById(id).orElseThrow(DataNotFoundException::new);
    File file = new File(attachment.getFilePath());

    if (file.exists() && file.length() > 0) {
      byte[] bytes = FileUtils.readFileToByteArray(file);

      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.setContentLength(bytes.length);
      httpHeaders.set("Content-Disposition",
          "attachment; filename=\"" + attachment.getEncodedFileName() + "\";");
      httpHeaders.set("Content-Transfer-Encoding", "binary");
      httpHeaders.setContentType(MediaType.valueOf(Files.probeContentType(file.toPath())));
      return new ResponseEntity(bytes, httpHeaders, HttpStatus.OK);
    }

    return ResponseEntity.notFound().build();
  }
}
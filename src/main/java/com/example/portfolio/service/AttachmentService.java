package com.example.portfolio.service;

import com.example.portfolio.domain.Attachment;
import com.example.portfolio.repository.AttachmentRepository;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttachmentService {

  @NonNull
  private final AttachmentRepository attachmentRepository;

  @Cacheable(value = "attachment", key = "#id")
  public Optional<Attachment> findById(Long id) {
    return attachmentRepository.findById(id);
  }

  @CacheEvict(value = "attachment", key = "#attachment.id")
  public Attachment save(Attachment attachment) {
    return attachmentRepository.save(attachment);
  }

}

package com.example.portfolio.service;

import com.example.portfolio.domain.Attachment;
import com.example.portfolio.util.FileUtil;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileUploadService {

  public static final String PATH_LOGOS = "logos/";

  public static final String PATH_ATTACHMENTS = "attachments/";

  @Value("${spring.servlet.multipart.location}")
  private String rootPath;

  public Attachment upload(MultipartFile file, String destinationPath) throws IOException {
    if (file == null || file.isEmpty()) {
      return null;
    }

    String fileName = file.getOriginalFilename();
    String fileExtension = FilenameUtils.getExtension(fileName);
    if (StringUtils.isBlank(fileExtension)) {
      return null;
    }

    File destinationFile;
    String destinationFileName;
    do {
      destinationFileName = destinationPath + FileUtil.getRandomFilename(fileExtension);
      destinationFile = new File(rootPath + destinationFileName);
    } while (destinationFile.exists());

    //noinspection ResultOfMethodCallIgnored
    destinationFile.getParentFile().mkdirs();
    file.transferTo(destinationFile);

    Attachment attachment = new Attachment();

    //이미지인 경우 리사이징
    if (file.getContentType() != null && file.getContentType().contains("image")) {
      BufferedImage image = ImageIO.read(destinationFile);

      double maxThumbnailWidth = 1280.0;
      double thumbnailRatio =
          maxThumbnailWidth > image.getWidth() ? 1 : maxThumbnailWidth / image.getWidth();

      //이미지 사이즈 변경
      BufferedImage resizedImage = resize(image, thumbnailRatio, Image.SCALE_FAST);
      ImageIO.write(resizedImage, "JPEG", destinationFile);
    }

    attachment.setFileName(file.getOriginalFilename());
    attachment.setFilePath(FilenameUtils.normalize(destinationFile.getAbsolutePath(), true));
    attachment.setFileContentType(file.getContentType());
    attachment.setFileSize(file.getSize());
    return attachment;
  }

  public Attachment uploadFile(MultipartFile file) throws IOException {
    return upload(file, PATH_ATTACHMENTS);
  }

  public Attachment uploadLogo(MultipartFile file) throws IOException {
    return upload(file, PATH_LOGOS);
  }

  private BufferedImage resize(BufferedImage beforeImage, double ratio, int imageScale) {
    int newW = (int) (beforeImage.getWidth() * ratio);
    int newH = (int) (beforeImage.getHeight() * ratio);
    Image tmp = beforeImage.getScaledInstance(newW, newH, imageScale);
    BufferedImage dimg = new BufferedImage(newW, newH, beforeImage.getType());

    Graphics2D g2d = dimg.createGraphics();
    g2d.drawImage(tmp, 0, 0, null);
    g2d.dispose();

    return dimg;
  }
}

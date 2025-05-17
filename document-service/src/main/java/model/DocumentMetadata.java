package model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document("documents")
public class DocumentMetadata {

  @Id
  private String id;
  private String title;
  private String originalFilename;
  private String contentType;
  private String uploader;
  private List<String> tags;
  private String gridFsFileId;
  private boolean deleted;
  private Instant uploadedAt;

  public DocumentMetadata() {
  }

  public DocumentMetadata(String id, String title, String originalFilename, String contentType,
                          String uploader, List<String> tags, String gridFsFileId,
                          boolean deleted, Instant uploadedAt) {
    this.id = id;
    this.title = title;
    this.originalFilename = originalFilename;
    this.contentType = contentType;
    this.uploader = uploader;
    this.tags = tags;
    this.gridFsFileId = gridFsFileId;
    this.deleted = deleted;
    this.uploadedAt = uploadedAt;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getOriginalFilename() {
    return originalFilename;
  }

  public void setOriginalFilename(String originalFilename) {
    this.originalFilename = originalFilename;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getUploader() {
    return uploader;
  }

  public void setUploader(String uploader) {
    this.uploader = uploader;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public String getGridFsFileId() {
    return gridFsFileId;
  }

  public void setGridFsFileId(String gridFsFileId) {
    this.gridFsFileId = gridFsFileId;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public Instant getUploadedAt() {
    return uploadedAt;
  }

  public void setUploadedAt(Instant uploadedAt) {
    this.uploadedAt = uploadedAt;
  }

  @Override
  public String toString() {
    return "DocumentMetadata{" +
      "id='" + id + '\'' +
      ", title='" + title + '\'' +
      ", originalFilename='" + originalFilename + '\'' +
      ", contentType='" + contentType + '\'' +
      ", uploader='" + uploader + '\'' +
      ", tags=" + tags +
      ", gridFsFileId='" + gridFsFileId + '\'' +
      ", deleted=" + deleted +
      ", uploadedAt=" + uploadedAt +
      '}';
  }
}


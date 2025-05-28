package com.deepak.dms.document_service.service;

import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.deepak.dms.document_service.model.DocumentMetadata;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.deepak.dms.document_service.repository.DocumentMetadataRepository;
import com.mongodb.client.gridfs.GridFSBucket;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Service
public class DocumentService {

  @Autowired
  private GridFsTemplate gridFsTemplate;

  @Autowired
  private GridFSBucket gridFsBucket;

  @Autowired
  private DocumentMetadataRepository documentMetadataRepository;

  // 🔼 Upload document
  public String uploadDocument(MultipartFile file, String title, List<String> tags, String uploader) throws IOException {
    DBObject metaData = new BasicDBObject();
    metaData.put("contentType", file.getContentType());

    ObjectId gridFsFileId = gridFsTemplate.store(
      file.getInputStream(),
      file.getOriginalFilename(),
      file.getContentType(),
      metaData
    );

    DocumentMetadata metadata = new DocumentMetadata();
    metadata.setTitle(title);
    metadata.setOriginalFilename(file.getOriginalFilename());
    metadata.setContentType(file.getContentType());
    metadata.setUploader(uploader);
    metadata.setTags(tags);
    metadata.setGridFsFileId(gridFsFileId.toHexString());
    metadata.setUploadedAt(Instant.now());
    metadata.setDeleted(false);

    DocumentMetadata saved = documentMetadataRepository.save(metadata);
    return saved.getId();
  }

  // ⬇️ Download document stream
  public GridFSDownloadStream downloadDocument(String metadataId) {
    DocumentMetadata metadata = documentMetadataRepository.findById(metadataId)
      .orElseThrow(() -> new RuntimeException("Metadata not found"));

    GridFSFile file = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(new ObjectId(metadata.getGridFsFileId()))));
    if (file == null) throw new RuntimeException("File not found in GridFS");

    return gridFsBucket.openDownloadStream(file.getObjectId());
  }

  // 📄 Get document metadata
  public DocumentMetadata getDocumentMetadata(String id) {
    return documentMetadataRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Metadata not found"));
  }

  // 🛠 Update metadata
  public DocumentMetadata updateDocumentMetadata(String id, DocumentMetadata updated) {
    DocumentMetadata existing = documentMetadataRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Metadata not found"));

    existing.setTitle(updated.getTitle());
    existing.setTags(updated.getTags());
    existing.setUploader(updated.getUploader());

    return documentMetadataRepository.save(existing);
  }

  // ❌ Soft delete
  public void deleteDocument(String id) {
    DocumentMetadata metadata = documentMetadataRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Metadata not found"));

    metadata.setDeleted(true);
    documentMetadataRepository.save(metadata);
  }

  // 🔁 Restore soft-deleted document
  public void restoreDocument(String id) {
    DocumentMetadata metadata = documentMetadataRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Metadata not found"));

    metadata.setDeleted(false);
    documentMetadataRepository.save(metadata);
  }

  // 📋 List active (non-deleted) documents
  public List<DocumentMetadata> getAllActiveDocuments() {
    return documentMetadataRepository.findByDeletedFalse();
  }
}

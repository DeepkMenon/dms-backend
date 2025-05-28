package com.deepak.dms.document_service.repository;

import com.deepak.dms.document_service.model.DocumentMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DocumentMetadataRepository extends MongoRepository<DocumentMetadata, String> {
  List<DocumentMetadata> findByDeletedFalse();
}


package repository;

import model.DocumentMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DocumentMetadataRepository extends MongoRepository<DocumentMetadata, String> {
  List<DocumentMetadata> findByDeletedFalse();
}


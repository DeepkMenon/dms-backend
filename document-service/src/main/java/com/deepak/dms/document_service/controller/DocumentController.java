package com.deepak.dms.document_service.controller;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import jakarta.servlet.http.HttpServletResponse;
import com.deepak.dms.document_service.model.DocumentMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
  import org.springframework.web.multipart.MultipartFile;
import com.deepak.dms.document_service.service.DocumentService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

  @Autowired
  private DocumentService documentService;

  // 🔼 Upload a single document
  @PostMapping("/upload")
  public ResponseEntity<String> uploadDocument(
    @RequestParam("file") MultipartFile file,
    @RequestParam("title") String title,
    @RequestParam("tags") List<String> tags,
    @RequestParam("uploader") String uploader
  ) throws IOException {
    String documentId = documentService.uploadDocument(file, title, tags, uploader);
    return ResponseEntity.ok(documentId);
  }

  // ⬇️ Download document content
  @GetMapping("/{id}/download")
  public void downloadDocument(@PathVariable String id, HttpServletResponse response) throws IOException {
    GridFSDownloadStream stream = documentService.downloadDocument(id);
    DocumentMetadata metadata = documentService.getDocumentMetadata(id);

    response.setContentType(metadata.getContentType());
    response.setHeader("Content-Disposition", "attachment; filename=\"" + metadata.getOriginalFilename() + "\"");

    try (OutputStream os = response.getOutputStream()) {
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = stream.read(buffer)) != -1) {
        os.write(buffer, 0, bytesRead);
      }
    }

    stream.close();
  }

  // 📄 Get metadata
  @GetMapping("/{id}")
  public ResponseEntity<DocumentMetadata> getDocument(@PathVariable String id) {
    DocumentMetadata metadata = documentService.getDocumentMetadata(id);
    return ResponseEntity.ok(metadata);
  }

  // 🛠 Update metadata
  @PutMapping("/{id}")
  public ResponseEntity<DocumentMetadata> updateMetadata(
    @PathVariable String id,
    @RequestBody DocumentMetadata updated
  ) {
    DocumentMetadata updatedDoc = documentService.updateDocumentMetadata(id, updated);
    return ResponseEntity.ok(updatedDoc);
  }

  // ❌ Soft delete
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteDocument(@PathVariable String id) {
    documentService.deleteDocument(id);
    return ResponseEntity.ok("Document soft-deleted");
  }

  // 🔁 Restore a soft-deleted document
  @PostMapping("/{id}/restore")
  public ResponseEntity<String> restoreDocument(@PathVariable String id) {
    documentService.restoreDocument(id);
    return ResponseEntity.ok("Document restored");
  }

  // 📋 List all active documents
  @GetMapping
  public ResponseEntity<List<DocumentMetadata>> listDocuments() {
    List<DocumentMetadata> documents = documentService.getAllActiveDocuments();
    return ResponseEntity.ok(documents);
  }

}



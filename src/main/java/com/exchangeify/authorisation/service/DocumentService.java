package com.exchangeify.authorisation.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.tika.exception.TikaException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.exchangeify.authorisation.model.Document;

public interface DocumentService {
    Document ingestDocument(MultipartFile file, String author, Map<String, String> additionalMetadata) throws IOException, TikaException, SAXException;

    List<Document> searchDocuments(String keyword);

    Page<Document> filterDocuments(String author, String fileType, Pageable pageable);

    Optional<Document> getDocumentById(Long id);
}

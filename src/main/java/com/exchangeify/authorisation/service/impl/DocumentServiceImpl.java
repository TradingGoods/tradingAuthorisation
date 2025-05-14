package com.exchangeify.authorisation.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.exchangeify.authorisation.model.Document;
import com.exchangeify.authorisation.repository.DocumentRepository;
import com.exchangeify.authorisation.service.DocumentService;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document ingestDocument(MultipartFile file, String author, Map<String, String> additionalMetadata) throws IOException, TikaException, SAXException {
        String content = extractContent(file);
        Document document = new Document();
        document.setTitle(file.getOriginalFilename());
        document.setContent(content);
        document.setFileType(getFileExtension(file.getOriginalFilename()));
        document.setUploadDate(LocalDateTime.now());
        document.setAuthor(author);
        document.setMetadata(additionalMetadata);
        // You might want to extract more metadata using Tika
        return documentRepository.save(document);
    }

    private String extractContent(MultipartFile file) throws IOException, TikaException, SAXException {
        Parser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler(-1); // -1 for unlimited text
        Metadata metadata = new Metadata();
        try (InputStream stream = file.getInputStream()) {
            parser.parse(stream, handler, metadata, new ParseContext());
        }
        return handler.toString();
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    public List<Document> searchDocuments(String keyword) {
        // Choose between basic keyword search or full-text search based on your database and requirements
        return documentRepository.searchByKeyword("%" + keyword.toLowerCase() + "%");
        // return documentRepository.fullTextSearch(keyword);
    }

    public Page<Document> filterDocuments(String author, String fileType, Pageable pageable) {
        System.out.println("Author type: " + (author != null ? author.getClass() : "null"));
        System.out.println("FileType type: " + (fileType != null ? fileType.getClass() : "null"));
        if(author == null && fileType == null) {
            return documentRepository.findAll(pageable);
        }
        if(author != null && fileType == null) {
            return documentRepository.findByAuthorIgnoreCase(author, pageable);
        }
        if(author == null && fileType != null) {
            return documentRepository.findByFileTypeIgnoreCase(fileType, pageable);
        }
    
        return documentRepository.filterDocuments(author, fileType, pageable);
    }

    public Optional<Document> getDocumentById(Long id) {
        return documentRepository.findById(id);
    }
}
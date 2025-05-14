package com.exchangeify.authorisation.controller;

import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.exchangeify.authorisation.model.Document;
import com.exchangeify.authorisation.service.DocumentService;

import jakarta.servlet.ServletException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.apache.tika.exception.TikaException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }
    
    @GetMapping("/hello")
    public String hello() throws ServletException {
        return "Hello, authenticated user!";
    }

    @PostMapping("/ingest")
    public ResponseEntity<Document> ingestDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "metadata", required = false) Map<String, String> metadata) {
            
        try { 
                Document ingestedDocument;
                ingestedDocument = documentService.ingestDocument(file,author, metadata != null ? metadata : Map.of());
                return new ResponseEntity<>(ingestedDocument, HttpStatus.CREATED);
        } catch (IOException | TikaException | SAXException  e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Document>> searchDocuments(@RequestParam("query") String query) {
        List<Document> results = documentService.searchDocuments(query);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<Document>> filterDocuments(
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "fileType", required = false) String fileType,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<Document> filteredDocuments = documentService.filterDocuments(author, fileType, pageable);
        return ResponseEntity.ok(filteredDocuments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        Optional<Document> document = documentService.getDocumentById(id);
        return document.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

package com.exchangeify.authorisation.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exchangeify.authorisation.model.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    // Basic keyword search using ILIKE (case-insensitive)
    @Query("SELECT d FROM Document d WHERE LOWER(d.content) LIKE %:keyword%")
    List<Document> searchByKeyword(@Param("keyword") String keyword);

    // More advanced search using CONTAINS (PostgreSQL) or MATCH AGAINST (MySQL - requires fulltext index)
    @Query(value = "SELECT * FROM document WHERE plainto_tsquery('english', :keyword) @@ to_tsvector('english', content)", nativeQuery = true)
    List<Document> fullTextSearch(@Param("keyword") String keyword);

    // Filter by metadata
    Page<Document> findByMetadata(Map<String, String> metadata, Pageable pageable);

    // Example of filtering by specific metadata fields
    Page<Document> findByAuthorIgnoreCase(String author, Pageable pageable);
    Page<Document> findByFileTypeIgnoreCase(String fileType, Pageable pageable);
    Page<Document> findByUploadDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // Combined filtering (example - you might need more dynamic queries for complex filtering)
    @Query("SELECT d FROM Document d WHERE (:author is null or lower(d.author) = lower(:author)) AND (:fileType is null or lower(d.fileType) = lower(:fileType))")
    Page<Document> filterDocuments(@Param("author") String author, @Param("fileType") String fileType, Pageable pageable);    

}

package org.capps.news.repository;

import org.capps.news.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArticleRepository extends JpaRepository<Article, UUID> {
    Page<Article> findByPublishedTrue(Pageable pageable);

    @Query("SELECT a FROM Article a WHERE a.category.id = :categoryId ORDER BY a.id DESC LIMIT 1")
    Optional<Article> findLatestArticleByCategoryId(@Param("categoryId") Long categoryId);

    List<Article> findByCategoryId(Long categoryId);

    @Query("SELECT a FROM Article a ORDER BY a.id DESC LIMIT 1")
    Optional<Article> findLatestArticle();


    List<Article> findByUser_Username(String username);
    List<Article> findByUser_UsernameAndPublishedTrue(String username);

    @Query("SELECT a FROM Article a WHERE " +
            "a.published = true AND " +
            "(LOWER(a.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(a.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(a.content) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Article> searchPublishedArticles(@Param("query") String query);

    // Méthode de recherche avancée avec spécification
    @Query("SELECT a FROM Article a WHERE " +
            "(:query IS NULL OR " +
            "(LOWER(a.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(a.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(a.content) LIKE LOWER(CONCAT('%', :query, '%')))) AND " +
            "(:categoryId IS NULL OR a.category.id = :categoryId) AND " +
            "(:authorUsername IS NULL OR a.user.username = :authorUsername) AND " +
            "(:startDate IS NULL OR a.publicationDate >= :startDate) AND " +
            "(:endDate IS NULL OR a.publicationDate <= :endDate)")
    List<Article> advancedSearch(
            @Param("query") String query,
            @Param("categoryId") Long categoryId,
            @Param("authorUsername") String authorUsername,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
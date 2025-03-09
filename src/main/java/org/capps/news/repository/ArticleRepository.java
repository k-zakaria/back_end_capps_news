package org.capps.news.repository;

import org.capps.news.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArticleRepository extends JpaRepository<Article, UUID> {
    @Override
    Page<Article> findAll(Pageable pageable);

    @Query("SELECT a FROM Article a WHERE a.category.id = :categoryId ORDER BY a.id DESC LIMIT 1")
    Optional<Article> findLatestArticleByCategoryId(@Param("categoryId") Long categoryId);

    List<Article> findByCategoryId(Long categoryId);

    @Query("SELECT a FROM Article a ORDER BY a.id DESC LIMIT 1")
    Optional<Article> findLatestArticle();
}
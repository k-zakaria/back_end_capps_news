package org.capps.news.repository;

import org.capps.news.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArticleRepository extends JpaRepository<Article, UUID> {
}
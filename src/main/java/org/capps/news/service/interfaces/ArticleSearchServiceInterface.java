package org.capps.news.service.interfaces;

import org.capps.news.model.Article;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleSearchServiceInterface {
    List<Article> searchPublishedArticles(String query);
    List<Article> advancedSearch(
            String query,
            Long categoryId,
            String authorUsername,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}

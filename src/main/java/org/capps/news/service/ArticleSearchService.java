package org.capps.news.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.capps.news.model.Article;
import org.capps.news.repository.ArticleRepository;
import org.capps.news.service.interfaces.ArticleSearchServiceInterface;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleSearchService implements ArticleSearchServiceInterface {
    private final ArticleRepository articleRepository;

    @Override
    public List<Article> searchPublishedArticles(String query) {
        if (StringUtils.isEmpty(query)) {
            return Collections.emptyList();
        }
        return articleRepository.searchPublishedArticles(query);
    }

    @Override
    public List<Article> advancedSearch(
            String query,
            Long categoryId,
            String authorUsername,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        query = StringUtils.hasText(query) ? query : null;
        authorUsername = StringUtils.hasText(authorUsername) ? authorUsername : null;

        return articleRepository.advancedSearch(
                query,
                categoryId,
                authorUsername,
                startDate,
                endDate
        );
    }
}

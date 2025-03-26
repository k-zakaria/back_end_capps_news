package org.capps.news.service.interfaces;

import jakarta.transaction.Transactional;
import org.capps.news.model.Article;
import org.capps.news.model.User;
import org.capps.news.service.dto.PaginatedResponse;
import org.capps.news.web.vm.response.ArticleResVM;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArticleServiceInterface {

    List<Article> getAllArticles();

    Optional<Article> getArticleById(UUID id);

    Optional<Article> getLatestArticleByCategoryId(Long categoryId);

    Optional<Article> getLatestArticle();

    List<Article> getArticlesByCategoryId(Long categoryId);

    List<Article> getArticlesByUsername(String username);

    List<Article> getArticlesByAuthor(String username, Authentication authentication);

    List<Article> getPublishedArticlesByAuthor(String username);

    Article createArticle(Article article, MultipartFile images, User currentUser);

    Article updateArticle(UUID articleId, Article updatedArticle, MultipartFile image, User currentUser);

    void deleteArticle(UUID articleId, User currentUser);

    Article publishArticle(UUID articleId, Authentication authentication);

    Article unpublishArticle(UUID articleId, Authentication authentication);

    List<Article> searchPublishedArticles(String query);

    List<Article> advancedSearch(
            String query,
            Long categoryId,
            String authorUsername,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    PaginatedResponse<ArticleResVM> getPaginatedArticles(
            int page,
            int size,
            String sortBy,
            String sortDir
    );
}

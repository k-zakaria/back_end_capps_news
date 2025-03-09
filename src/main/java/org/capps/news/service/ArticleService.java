package org.capps.news.service;

import lombok.AllArgsConstructor;
import org.capps.news.aws.StorageService;
import org.capps.news.model.Article;
import org.capps.news.model.Category;
import org.capps.news.model.Tag;
import org.capps.news.model.User;
import org.capps.news.repository.ArticleRepository;
import org.capps.news.repository.CategoryRepository;
import org.capps.news.repository.TagRepository;
import org.capps.news.repository.UserRepository;
import org.capps.news.web.exception.artilce.ArticleNotFoundException;
import org.capps.news.web.exception.category.CategoryNotFoundException;
import org.capps.news.web.exception.tag.TagNotFoundException;
import org.capps.news.web.exception.user.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@AllArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    private final StorageService storageService;



    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Optional<Article> getArticleById(UUID id) {
        return articleRepository.findById(id);
    }

    public Optional<Article> getLatestArticleByCategoryId(Long categoryId) {
        return articleRepository.findLatestArticleByCategoryId(categoryId);
    }

    public Optional<Article> getLatestArticle() {
        return articleRepository.findLatestArticle();
    }

    public List<Article> getArticlesByCategoryId(Long categoryId) {
        return articleRepository.findByCategoryId(categoryId);
    }

    public Article createArticle(Article article , MultipartFile images) {
        User user = userRepository.findById(article.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Category category = categoryRepository.findById(article.getCategory().getId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        Set<Tag> tags = new HashSet<>();
        for (Tag tag : article.getTags()) {
            Tag existingTag = tagRepository.findById(tag.getId())
                    .orElseThrow(() -> new TagNotFoundException("Tag not found"));
            tags.add(existingTag);
        }

        List<Tag> tagList = new ArrayList<>(tags);

        article.setTags(tagList);

        article.setImage(storageService.uploadFile(images));

        article.setUser(user);
        article.setCategory(category);

        return articleRepository.save(article);
    }

    public Article updateArticle(UUID articleId, Article updatedArticle) {
        Article existingArticle = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("Article not found"));

        existingArticle.setTitle(updatedArticle.getTitle());
        existingArticle.setContent(updatedArticle.getContent());

        if (updatedArticle.getUser() != null) {
            User user = userRepository.findById(updatedArticle.getUser().getId())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            existingArticle.setUser(user);
        }

        if (updatedArticle.getCategory() != null) {
            Category category = categoryRepository.findById(updatedArticle.getCategory().getId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
            existingArticle.setCategory(category);
        }

        if (updatedArticle.getTags() != null && !updatedArticle.getTags().isEmpty()) {
            List<Tag> updatedTags = new ArrayList<>();
            for (Tag tag : updatedArticle.getTags()) {
                Tag existingTag = tagRepository.findById(tag.getId())
                        .orElseThrow(() -> new TagNotFoundException("Tag not found"));
                updatedTags.add(existingTag);
            }
            existingArticle.setTags(updatedTags);
        }

        return articleRepository.save(existingArticle);
    }

    public void deleteArticle(UUID id) {
        articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Article not found"));
        articleRepository.deleteById(id);
    }
}
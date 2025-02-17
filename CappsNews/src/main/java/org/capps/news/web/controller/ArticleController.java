package org.capps.news.web.controller;

import org.capps.news.model.Article;
import org.capps.news.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping(("/articles"))
    public List<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("article/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable UUID id) {
        return articleService.getArticleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(("/article"))
    public Article createArticle(@RequestBody Article article) {
        return articleService.createArticle(article);
    }

    @PutMapping("/article/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable UUID id, @RequestBody Article articleDetails) {
        return ResponseEntity.ok(articleService.updateArticle(id, articleDetails));
    }

    @DeleteMapping("/article/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable UUID id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}
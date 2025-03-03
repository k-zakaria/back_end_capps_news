package org.capps.news.web.controller;

import org.capps.news.model.Article;
import org.capps.news.service.ArticleService;
import org.capps.news.web.vm.mapper.ArticleVMMapper;
import org.capps.news.web.vm.request.ArticleReqVM;
import org.capps.news.web.vm.response.ArticleResVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/")
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleVMMapper articleVMMapper;

    @Autowired
    public ArticleController(ArticleService articleService, ArticleVMMapper articleVMMapper) {
        this.articleService = articleService;
        this.articleVMMapper = articleVMMapper;
    }

    @GetMapping("article/{id}")
    public ResponseEntity<ArticleResVM> getArticleById(@PathVariable UUID id) {
        return articleService.getArticleById(id)
                .map(articleVMMapper::articleToArticleResVM) // Conversion avec MapStruct
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/articles")
    public List<ArticleResVM> getAllArticles() {
        return articleService.getAllArticles().stream()
                .map(articleVMMapper::articleToArticleResVM)
                .collect(Collectors.toList());
    }

    @GetMapping("article/category/{categoryId}/latest")
    public ResponseEntity<ArticleResVM> getLatestArticleByCategoryId(@PathVariable Long categoryId) {
        return articleService.getLatestArticleByCategoryId(categoryId)
                .map(articleVMMapper::articleToArticleResVM)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/articles/category/{categoryId}")
    public ResponseEntity<List<ArticleResVM>> getArticlesByCategoryId(@PathVariable Long categoryId) {
        List<ArticleResVM> articles = articleService.getArticlesByCategoryId(categoryId).stream()
                .map(articleVMMapper::articleToArticleResVM)
                .collect(Collectors.toList());

        if (articles.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("article/latest")
    public ResponseEntity<ArticleResVM> getLatestArticle() {
        return articleService.getLatestArticle()
                .map(articleVMMapper::articleToArticleResVM)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/article")
    public ResponseEntity<ArticleResVM> createArticle(@RequestBody ArticleReqVM articleReqVM) {
        Article article = articleVMMapper.articleReqVMToArticle(articleReqVM);
        Article createdArticle = articleService.createArticle(article);
        ArticleResVM response = articleVMMapper.articleToArticleResVM(createdArticle);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/article/{id}")
    public ResponseEntity<ArticleResVM> updateArticle(@PathVariable UUID id, @RequestBody ArticleReqVM articleReqVM) {
        Article article = articleVMMapper.articleReqVMToArticle(articleReqVM);
        Article updatedArticle = articleService.updateArticle(id, article);
        ArticleResVM response = articleVMMapper.articleToArticleResVM(updatedArticle);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/article/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable UUID id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}
package org.capps.news.web.controller;

import lombok.RequiredArgsConstructor;
import org.capps.news.model.Article;
import org.capps.news.service.ArticleSearchService;
import org.capps.news.service.interfaces.ArticleSearchServiceInterface;
import org.capps.news.web.vm.mapper.ArticleVMMapper;
import org.capps.news.web.vm.response.ArticleResVM;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleSearchController {
    private final ArticleSearchServiceInterface articleSearchService;
    private final ArticleVMMapper articleVMMapper;

    @GetMapping("/search")
    public ResponseEntity<List<ArticleResVM>> searchArticles(
            @RequestParam(value = "q", required = false) String query
    ) {
        List<Article> articles = articleSearchService.searchPublishedArticles(query);

        List<ArticleResVM> articleResVMs = articles.stream()
                .map(articleVMMapper::articleToArticleResVM)
                .collect(Collectors.toList());

        return ResponseEntity.ok(articleResVMs);
    }

    @GetMapping("/advanced-search")
    public ResponseEntity<List<ArticleResVM>> advancedSearch(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "authorUsername", required = false) String authorUsername,
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        List<Article> articles = articleSearchService.advancedSearch(
                query,
                categoryId,
                authorUsername,
                startDate,
                endDate
        );

        List<ArticleResVM> articleResVMs = articles.stream()
                .map(articleVMMapper::articleToArticleResVM)
                .collect(Collectors.toList());

        return ResponseEntity.ok(articleResVMs);
    }
}
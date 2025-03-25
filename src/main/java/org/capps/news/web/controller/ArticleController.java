package org.capps.news.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.capps.news.aws.StorageService;
import org.capps.news.model.Article;
import org.capps.news.model.User;
import org.capps.news.repository.UserRepository;
import org.capps.news.service.ArticleService;
import org.capps.news.service.UserService;
import org.capps.news.service.dto.PaginatedResponse;
import org.capps.news.service.interfaces.ArticleServiceInterface;
import org.capps.news.web.exception.user.UserNotFoundException;
import org.capps.news.web.vm.mapper.ArticleVMMapper;
import org.capps.news.web.vm.request.ArticleReqVM;
import org.capps.news.web.vm.response.ArticleResVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/")
@AllArgsConstructor
public class ArticleController {

    private final ArticleServiceInterface articleService;
    private final ArticleVMMapper articleVMMapper;

    private final UserService userService;

    private final StorageService serviceAws ;
    private final UserRepository userRepository;


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

    @GetMapping("/author/{username}")
    public ResponseEntity<List<ArticleResVM>> getArticlesByAuthor(
            @PathVariable String username,
            Authentication authentication
    ) {
        List<Article> articles = articleService.getArticlesByAuthor(username, authentication);

        List<ArticleResVM> articleResVMs = articles.stream()
                .map(articleVMMapper::articleToArticleResVM)
                .collect(Collectors.toList());

        return ResponseEntity.ok(articleResVMs);
    }

    @GetMapping("/author/{username}/published")
    public ResponseEntity<List<ArticleResVM>> getPublishedArticlesByAuthor(
            @PathVariable String username
    ) {
        List<Article> articles = articleService.getPublishedArticlesByAuthor(username);

        List<ArticleResVM> articleResVMs = articles.stream()
                .map(articleVMMapper::articleToArticleResVM)
                .collect(Collectors.toList());

        return ResponseEntity.ok(articleResVMs);
    }

    @PostMapping("/article")
    public ResponseEntity<ArticleResVM> createArticle(
            @RequestParam("article") String articleJson,
            @RequestParam("image") MultipartFile images
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ArticleReqVM articleReqVM = objectMapper.readValue(articleJson, ArticleReqVM.class);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsernameAndDeletedFalse(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));

        Article article = articleVMMapper.articleReqVMToArticle(articleReqVM);
        article.setUser(currentUser);

        Article createdArticle = articleService.createArticle(article, images, currentUser);

        return ResponseEntity.ok(articleVMMapper.articleToArticleResVM(createdArticle));
    }

    @PutMapping("article/{articleId}")
    public ResponseEntity<ArticleResVM> updateArticle(
            @PathVariable UUID articleId,
            @RequestPart("article") String articleJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ArticleReqVM articleReqVM = objectMapper.readValue(articleJson, ArticleReqVM.class);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsernameAndDeletedFalse(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));

        Article article = articleVMMapper.articleReqVMToArticle(articleReqVM);
        Article updatedArticle = articleService.updateArticle(articleId, article, image, currentUser);

        return ResponseEntity.ok(articleVMMapper.articleToArticleResVM(updatedArticle));
    }

    @DeleteMapping("article/{articleId}")
    public ResponseEntity<Void> deleteArticle(@PathVariable UUID articleId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsernameAndDeletedFalse(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));

        articleService.deleteArticle(articleId, currentUser);

        return ResponseEntity.noContent().build();
    }


    @PutMapping("/article/{articleId}/publish")
    public ResponseEntity<ArticleResVM> publishArticle(
            @PathVariable UUID articleId,
            Authentication authentication
    ) {
        Article publishedArticle = articleService.publishArticle(articleId, authentication);
        return ResponseEntity.ok(articleVMMapper.articleToArticleResVM(publishedArticle));
    }

    @PutMapping("/article/{articleId}/unpublish")
    public ResponseEntity<ArticleResVM> unpublishArticle(
            @PathVariable UUID articleId,
            Authentication authentication
    ) {
        Article unpublishedArticle = articleService.unpublishArticle(articleId, authentication);
        return ResponseEntity.ok(articleVMMapper.articleToArticleResVM(unpublishedArticle));
    }

    @GetMapping("/articles/paginated")
    public ResponseEntity<PaginatedResponse<ArticleResVM>> getPaginatedArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "publicationDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        PaginatedResponse<ArticleResVM> response = articleService.getPaginatedArticles(
                page, size, sortBy, sortDir
        );
        return ResponseEntity.ok(response);
    }


}
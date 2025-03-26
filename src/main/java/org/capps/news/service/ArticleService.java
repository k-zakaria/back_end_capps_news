package org.capps.news.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.capps.news.aws.StorageService;
import org.capps.news.model.Article;
import org.capps.news.model.Category;
import org.capps.news.model.Tag;
import org.capps.news.model.User;
import org.capps.news.model.enums.Role;
import org.capps.news.repository.ArticleRepository;
import org.capps.news.repository.CategoryRepository;
import org.capps.news.repository.TagRepository;
import org.capps.news.repository.UserRepository;
import org.capps.news.service.dto.PaginatedResponse;
import org.capps.news.service.interfaces.ArticleServiceInterface;
import org.capps.news.web.exception.artilce.*;
import org.capps.news.web.exception.category.CategoryNotFoundException;
import org.capps.news.web.exception.tag.TagNotFoundException;
import org.capps.news.web.exception.user.UserNotFoundException;
import org.capps.news.web.vm.mapper.ArticleVMMapper;
import org.capps.news.web.vm.response.ArticleResVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ArticleService implements ArticleServiceInterface {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final ArticleVMMapper articleVMMapper;
    private final StorageService storageService;



    @Override
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @Override
    public Optional<Article> getArticleById(UUID id) {
        return articleRepository.findById(id);
    }

    @Override
    public Optional<Article> getLatestArticleByCategoryId(Long categoryId) {
        return articleRepository.findLatestArticleByCategoryId(categoryId);
    }

    @Override
    public Optional<Article> getLatestArticle() {
        return articleRepository.findLatestArticle();
    }

    @Override
    public List<Article> getArticlesByCategoryId(Long categoryId) {
        return articleRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Article> getArticlesByUsername(String username) {
        userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));

        return articleRepository.findByUser_Username(username);
    }

    @Override
    public List<Article> getArticlesByAuthor(String username, Authentication authentication) {
        validateArticleViewPermissions(username, authentication);

        return articleRepository.findByUser_Username(username);
    }

    @Override
    public List<Article> getPublishedArticlesByAuthor(String username) {
        return articleRepository.findByUser_UsernameAndPublishedTrue(username);
    }


    private void validateArticleViewPermissions(String username, Authentication authentication) {
        User currentUser = userRepository.findByUsernameAndDeletedFalse(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));

        boolean isAuthor = username.equals(currentUser.getUsername());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isAuthor && !isAdmin) {
            throw new UnauthorizedArticleAccessException("Vous n'avez pas la permission de voir ces articles");
        }
    }


    @Override
    @Transactional
    public Article createArticle(Article article, MultipartFile images, User currentUser) {

        if (!StringUtils.hasText(article.getTitle())) {
            throw new InvalidArticleException("Le titre de l'article est obligatoire");
        }
        if (!StringUtils.hasText(article.getContent())) {
            throw new InvalidArticleException("Le contenu de l'article est obligatoire");
        }

        Category category = categoryRepository.findById(article.getCategory().getId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        List<Tag> tags = article.getTags().stream()
                .map(tag -> tagRepository.findById(tag.getId())
                        .orElseThrow(() -> new TagNotFoundException("Tag not found")))
                .collect(Collectors.toList());

        String imageUrl = null;
        if (images != null && !images.isEmpty()) {
            imageUrl = storageService.uploadFile(images);
            System.out.println("Image téléchargée : " + imageUrl);
        } else {
            System.out.println("Aucune image fournie");
        }

        Article newArticle = Article.builder()
                .title(article.getTitle())
                .description(article.getDescription())
                .content(article.getContent())
                .image(imageUrl)
                .user(currentUser)
                .category(category)
                .tags(tags)
                .published(determinePublicationStatus(currentUser))
                .build();

        return articleRepository.save(newArticle);
    }


    private boolean determinePublicationStatus(User currentUser) {
        boolean shouldPublish = currentUser.getRole() == Role.ADMIN || currentUser.getRole() == Role.AUTHOR;
        System.out.println("Publication automatique pour l'utilisateur " + currentUser.getUsername() + " : " + shouldPublish);
        return shouldPublish;
    }


    private void updateArticleFields(Article existingArticle, Article updatedArticle) {
        if (StringUtils.hasText(updatedArticle.getTitle())) {
            existingArticle.setTitle(updatedArticle.getTitle());
        }

        if (StringUtils.hasText(updatedArticle.getDescription())) {
            existingArticle.setDescription(updatedArticle.getDescription());
        }

        if (StringUtils.hasText(updatedArticle.getContent())) {
            existingArticle.setContent(updatedArticle.getContent());
        }

        if (updatedArticle.isPublished() != existingArticle.isPublished()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = userRepository.findByUsernameAndDeletedFalse(authentication.getName())
                    .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));

            if (currentUser.getRole() == Role.ADMIN || currentUser.getRole() == Role.AUTHOR) {
                existingArticle.setPublished(updatedArticle.isPublished());

                if (updatedArticle.isPublished()) {
                    existingArticle.setPublicationDate(LocalDateTime.now());
                } else {
                    existingArticle.setPublicationDate(null);
                }
            }
        }

        existingArticle.setPublicationDate(LocalDateTime.now());
    }

    @Override
    public Article updateArticle(UUID articleId, Article updatedArticle, MultipartFile image, User currentUser) {
        Article existingArticle = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("Article non trouvé"));

        validateArticleUpdatePermissions(existingArticle, currentUser);

        updateArticleFields(existingArticle, updatedArticle);

        if (updatedArticle.getCategory() != null) {
            Category category = categoryRepository.findById(updatedArticle.getCategory().getId())
                    .orElseThrow(() -> new CategoryNotFoundException("Catégorie non trouvée"));
            existingArticle.setCategory(category);
        }

        if (updatedArticle.getTags() != null && !updatedArticle.getTags().isEmpty()) {
            List<Tag> tags = updatedArticle.getTags().stream()
                    .map(tag -> tagRepository.findById(tag.getId())
                            .orElseThrow(() -> new TagNotFoundException("Tag non trouvé")))
                    .collect(Collectors.toList());
            existingArticle.setTags(tags);
        }

        if (image != null && !image.isEmpty()) {
            if (StringUtils.hasText(existingArticle.getImage())) {
                storageService.deleteFile(existingArticle.getImage());
            }

            String newImageUrl = storageService.uploadFile(image);
            existingArticle.setImage(newImageUrl);
        }

        return articleRepository.save(existingArticle);
    }

    @Override
    public void deleteArticle(UUID articleId, User currentUser) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("Article non trouvé"));

        validateArticleDeletePermissions(article, currentUser);

        if (StringUtils.hasText(article.getImage())) {
            storageService.deleteFile(article.getImage());
        }

        articleRepository.delete(article);
    }

    private void validateArticleUpdatePermissions(Article article, User currentUser) {
        boolean isAuthorisation = article.getUser().getId() == currentUser.getId();
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        boolean isAuthor = currentUser.getRole() == Role.AUTHOR;

        if (!isAuthorisation && (!isAdmin || !isAuthor)) {
            throw new UnauthorizedArticleUpdateException("Vous n'avez pas la permission de modifier cet article");
        }
    }

    private void validateArticleDeletePermissions(Article article, User currentUser) {
        boolean isAuthorisation = article.getUser().getId() == currentUser.getId();
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        boolean isAuthor = currentUser.getRole() == Role.AUTHOR;

        if (!isAuthorisation && (!isAdmin || !isAuthor)) {
            throw new UnauthorizedArticleDeleteException("Vous n'avez pas la permission de supprimer cet article");
        }
    }

    @Override
    public Article publishArticle(UUID articleId, Authentication authentication) {
        // Récupérer l'article
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("Article non trouvé"));

        // Vérifier les permissions de publication
        validatePublicationPermissions(article, authentication);

        // Publier l'article
        article.setPublished(true);
        article.setPublicationDate(LocalDateTime.now());

        return articleRepository.save(article);
    }

    @Override
    public Article unpublishArticle(UUID articleId, Authentication authentication) {
        // Récupérer l'article
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("Article non trouvé"));

        // Vérifier les permissions de dépublication
        validatePublicationPermissions(article, authentication);

        // Dépublier l'article
        article.setPublished(false);
        article.setPublicationDate(null);

        return articleRepository.save(article);
    }


    private void validatePublicationPermissions(Article article, Authentication authentication) {
        // Récupérer l'utilisateur connecté
        User currentUser = userRepository.findByUsernameAndDeletedFalse(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));

        // Vérifier si l'utilisateur est l'auteur ou un admin
        boolean isAuthor = article.getUser().getId() == currentUser.getId();
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isAuthor && !isAdmin) {
            throw new UnauthorizedPublicationException("Vous n'avez pas la permission de publier/dépublier cet article");
        }
    }

    @Override
    public List<Article> searchPublishedArticles(String query) {
        // Validation de la requête
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
        // Validation et nettoyage des paramètres
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

    @Override
    public PaginatedResponse<ArticleResVM> getPaginatedArticles(
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {
        // Créer le tri
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Créer la pagination
        Pageable pageable = PageRequest.of(page, size, sort);

        // Récupérer la page d'articles
        Page<Article> articlePage = articleRepository.findByPublishedTrue(pageable);

        // Convertir en DTO
        List<ArticleResVM> articleResVMs = articlePage.getContent().stream()
                .map(articleVMMapper::articleToArticleResVM)
                .collect(Collectors.toList());

        // Construire la réponse paginée
        return new PaginatedResponse<>(
                articleResVMs,
                articlePage.getNumber(),
                articlePage.getSize(),
                articlePage.getTotalElements(),
                articlePage.getTotalPages(),
                articlePage.isLast()
        );
    }
}
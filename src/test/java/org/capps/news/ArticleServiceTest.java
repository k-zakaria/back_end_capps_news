package org.capps.news;

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
import org.capps.news.service.ArticleService;
import org.capps.news.web.exception.artilce.InvalidArticleException;
import org.capps.news.web.exception.category.CategoryNotFoundException;
import org.capps.news.web.exception.tag.TagNotFoundException;
import org.capps.news.web.vm.mapper.ArticleVMMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private StorageService storageService;

    @Mock
    private ArticleVMMapper articleVMMapper;

    @InjectMocks
    private ArticleService articleService;

    private User user;
    private Article article;
    private Category category;
    private Tag tag;
    private MultipartFile image;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(12345L);
        user.setUsername("testUser");
        user.setRole(Role.AUTHOR);

        category = new Category();
        category.setId(1L);
        category.setName("Test Category");

        tag = new Tag();
        tag.setId(1L);
        tag.setName("Test Tag");

        article = new Article();
        article.setId(UUID.randomUUID());
        article.setTitle("Test Article");
        article.setDescription("Test Description");
        article.setContent("Test Content");
        article.setUser(user);
        article.setCategory(category);
        article.setTags(Collections.singletonList(tag));
        article.setPublished(false);
    }

    @Test
    void getAllArticles_ShouldReturnAllArticles() {
        when(articleRepository.findAll()).thenReturn(Collections.singletonList(article));

        List<Article> articles = articleService.getAllArticles();

        assertNotNull(articles);
        assertEquals(1, articles.size());
        verify(articleRepository, times(1)).findAll();
    }

    @Test
    void getArticleById_ShouldReturnArticle() {
        when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));

        Optional<Article> foundArticle = articleService.getArticleById(article.getId());

        assertTrue(foundArticle.isPresent());
        assertEquals(article.getId(), foundArticle.get().getId());
        verify(articleRepository, times(1)).findById(article.getId());
    }

    @Test
    void getArticleById_ShouldReturnEmptyOptional() {
        when(articleRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        Optional<Article> foundArticle = articleService.getArticleById(UUID.randomUUID());

        assertFalse(foundArticle.isPresent());
        verify(articleRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void createArticle_ShouldCreateArticle() {
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(tagRepository.findById(tag.getId())).thenReturn(Optional.of(tag));
        when(storageService.uploadFile(image)).thenReturn("image_url");
        when(articleRepository.save(any(Article.class))).thenReturn(article);

        Article createdArticle = articleService.createArticle(article, image, user);

        assertNotNull(createdArticle);
        assertEquals(article.getTitle(), createdArticle.getTitle());
        verify(articleRepository, times(1)).save(any(Article.class));
    }


    @Test
    void createArticle_ShouldCreateArticleSuccessfully() {
        // Créer une image simulée
        MultipartFile image = mock(MultipartFile.class);
        when(image.isEmpty()).thenReturn(false); // Simuler une image non vide

        // Configurer les stubs
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(tagRepository.findById(tag.getId())).thenReturn(Optional.of(tag));
        when(storageService.uploadFile(image)).thenReturn("image_url");

        // Créer un article avec image = "image_url"
        Article savedArticle = Article.builder()
                .title(article.getTitle())
                .description(article.getDescription())
                .content(article.getContent())
                .image("image_url") // Simuler une image définie
                .user(user)
                .category(category)
                .tags(Collections.singletonList(tag))
                .published(true) // Simuler un article publié
                .build();

        when(articleRepository.save(any(Article.class))).thenReturn(savedArticle);

        // Appeler la méthode à tester
        Article createdArticle = articleService.createArticle(article, image, user);

        // Vérifier les résultats
        assertNotNull(createdArticle);
        assertEquals(article.getTitle(), createdArticle.getTitle());
        assertEquals("image_url", createdArticle.getImage()); // Vérifiez que l'image est définie
        assertTrue(createdArticle.isPublished()); // Vérifiez la publication automatique
        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    void createArticle_ShouldThrowCategoryNotFoundException() {
        // Arrange
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CategoryNotFoundException.class, () -> articleService.createArticle(article, image, user));
        verify(articleRepository, never()).save(any(Article.class));
    }

    @Test
    void createArticle_ShouldNotPublishForNonAuthorUsers() {
        // Arrange
        user.setRole(Role.AUTHOR); // Utilisateur non autorisé à publier
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(tagRepository.findById(tag.getId())).thenReturn(Optional.of(tag));
        when(articleRepository.save(any(Article.class))).thenReturn(article);

        // Act
        Article createdArticle = articleService.createArticle(article, null, user); // Pas d'image

        // Assert
        assertNotNull(createdArticle);
        assertFalse(createdArticle.isPublished()); // Vérifie que l'article n'est pas publié
    }

    @Test
    void createArticle_ShouldHandleMissingImage() {
        // Arrange
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(tagRepository.findById(tag.getId())).thenReturn(Optional.of(tag));
        when(articleRepository.save(any(Article.class))).thenReturn(article);

        // Act
        Article createdArticle = articleService.createArticle(article, null, user); // Pas d'image

        // Assert
        assertNotNull(createdArticle);
        assertNull(createdArticle.getImage()); // Vérifie que l'image est null
        verify(storageService, never()).uploadFile(any(MultipartFile.class));
    }

    @Test
    void createArticle_ShouldThrowExceptionForMissingTitle() {
        // Arrange
        article.setTitle(null); // Titre manquant

        // Act & Assert
        assertThrows(InvalidArticleException.class, () -> articleService.createArticle(article, image, user));
        verify(articleRepository, never()).save(any(Article.class));
    }

    @Test
    void createArticle_ShouldThrowExceptionForMissingContent() {
        // Arrange
        article.setContent(null); // Contenu manquant

        // Act & Assert
        assertThrows(InvalidArticleException.class, () -> articleService.createArticle(article, image, user));
        verify(articleRepository, never()).save(any(Article.class));
    }

    @Test
    void createArticle_ShouldThrowTagNotFoundException() {
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(tagRepository.findById(tag.getId())).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> articleService.createArticle(article, image, user));
    }

    @Test
    void updateArticle_ShouldUpdateArticle() {
        Article updatedArticle = new Article();
        updatedArticle.setTitle("Updated Title");
        updatedArticle.setDescription("Updated Description");
        updatedArticle.setContent("Updated Content");
        updatedArticle.setCategory(category);
        updatedArticle.setTags(Collections.singletonList(tag));

        when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(tagRepository.findById(tag.getId())).thenReturn(Optional.of(tag));
        // Supprimer le stub inutile
        // when(storageService.uploadFile(image)).thenReturn("new_image_url");
        when(articleRepository.save(any(Article.class))).thenReturn(article);

        Article result = articleService.updateArticle(article.getId(), updatedArticle, image, user);

        assertNotNull(result);
        assertEquals(updatedArticle.getTitle(), result.getTitle());
        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    void updateArticle_ShouldUpdateArticleWithImage() {
        // Créer une image simulée
        MultipartFile image = mock(MultipartFile.class);
        when(image.isEmpty()).thenReturn(false); // Simuler une image non vide

        Article updatedArticle = new Article();
        updatedArticle.setTitle("Updated Title");
        updatedArticle.setDescription("Updated Description");
        updatedArticle.setContent("Updated Content");
        updatedArticle.setCategory(category);
        updatedArticle.setTags(Collections.singletonList(tag));

        // Configurer les stubs
        when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(tagRepository.findById(tag.getId())).thenReturn(Optional.of(tag));
        when(storageService.uploadFile(image)).thenReturn("new_image_url");
        when(articleRepository.save(any(Article.class))).thenReturn(article);

        // Appeler la méthode à tester
        Article result = articleService.updateArticle(article.getId(), updatedArticle, image, user);

        // Vérifier les résultats
        assertNotNull(result);
        assertEquals(updatedArticle.getTitle(), result.getTitle());
        assertEquals("new_image_url", result.getImage()); // Vérifiez que l'image est mise à jour
        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    void deleteArticle_ShouldDeleteArticle() {
        when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));

        articleService.deleteArticle(article.getId(), user);

        verify(articleRepository, times(1)).delete(article);
    }

    @Test
    void publishArticle_ShouldPublishArticle() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser"); // Retourner un nom d'utilisateur non nul
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));
        when(userRepository.findByUsernameAndDeletedFalse("testUser")).thenReturn(Optional.of(user)); // Utiliser "testUser"
        when(articleRepository.save(any(Article.class))).thenReturn(article);

        Article publishedArticle = articleService.publishArticle(article.getId(), authentication);

        assertTrue(publishedArticle.isPublished());
        assertNotNull(publishedArticle.getPublicationDate());
        verify(articleRepository, times(1)).save(any(Article.class));
    }
}
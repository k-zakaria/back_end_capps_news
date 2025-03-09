package org.capps.news;

import org.capps.news.model.Article;
import org.capps.news.model.Category;
import org.capps.news.model.Tag;
import org.capps.news.model.User;
import org.capps.news.repository.ArticleRepository;
import org.capps.news.repository.CategoryRepository;
import org.capps.news.repository.TagRepository;
import org.capps.news.repository.UserRepository;
import org.capps.news.service.ArticleService;
import org.capps.news.web.exception.artilce.ArticleNotFoundException;
import org.capps.news.web.exception.category.CategoryNotFoundException;
import org.capps.news.web.exception.user.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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

    @InjectMocks
    private ArticleService articleService;

    private Article article;
    private User user;
    private Category category;
    private Tag tag1, tag2;


    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        category = new Category();
        category.setId(2L);

        tag1 = new Tag();
        tag1.setId(3L);

        tag2 = new Tag();
        tag2.setId(3L);

        article = new Article();
        article.setUser(user);
        article.setCategory(category);
        article.setTags(new ArrayList<>(Arrays.asList(tag1, tag2)));
    }
    // Test for getAllArticles
    @Test
    public void testGetAllArticles() {
        // Arrange
        List<Article> articles = Arrays.asList(new Article(), new Article());
        when(articleRepository.findAll()).thenReturn(articles);

        // Act
        List<Article> result = articleService.getAllArticles();

        // Assert
        assertEquals(2, result.size());
        verify(articleRepository, times(1)).findAll();
    }

    // Test for getArticleById (Article Found)
    @Test
    public void testGetArticleById_Found() {
        // Arrange
        UUID id = UUID.randomUUID();
        Article article = new Article();
        when(articleRepository.findById(id)).thenReturn(Optional.of(article));

        // Act
        Optional<Article> result = articleService.getArticleById(id);

        // Assert
        assertTrue(result.isPresent());
        verify(articleRepository, times(1)).findById(id);
    }

    // Test for getArticleById (Article Not Found)
    @Test
    public void testGetArticleById_NotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(articleRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Article> result = articleService.getArticleById(id);

        // Assert
        assertFalse(result.isPresent());
        verify(articleRepository, times(1)).findById(id);
    }

    // Test for createArticle
//    @Test
//    public void testCreateArticle() {
//        // Arrange
//        User user = new User();
//        user.setId(1L);
//
//        Category category = new Category();
//        category.setId(2L);
//
//        Tag tag1 = new Tag();
//        tag1.setId(3L);
//        Tag tag2 = new Tag();
//        tag2.setId(4L);
//
//
//        Article article = new Article();
//        article.setUser(user);
//        article.setCategory(category);
//        article.setTags(Arrays.asList(tag1, tag2));
//
//        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
//        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
//        when(tagRepository.findById(tag1.getId())).thenReturn(Optional.of(tag1));
//        when(tagRepository.findById(tag2.getId())).thenReturn(Optional.of(tag2));
//        when(articleRepository.save(article)).thenReturn(article);
//
//        // Act
//        Article result = articleService.createArticle(article);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(user, result.getUser());
//        assertEquals(category, result.getCategory());
//        assertEquals(2, result.getTags().size());
//        verify(articleRepository, times(1)).save(article);
//    }


//    @Test
//    void shouldThrowUserNotFoundException() {
//        when(userRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(UserNotFoundException.class, () -> articleService.createArticle(article));
//    }
//
//    @Test
//    void shouldThrowCategoryAlreadyExistsException() {
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());
//
//        assertThrows(CategoryNotFoundException.class, () -> articleService.createArticle(article));
//    }


    // Test for updateArticle
    @Test
    public void testUpdateArticle() {
        // Arrange
        UUID articleId = UUID.randomUUID();

        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(2L);

        Tag tag1 = new Tag();
        tag1.setId(3L);
        Tag tag2 = new Tag();
        tag2.setId(4L);

        Article existingArticle = new Article();
        existingArticle.setId(articleId);
        existingArticle.setTitle("Old Title");
        existingArticle.setContent("Old Content");

        Article updatedArticle = new Article();
        updatedArticle.setTitle("New Title");
        updatedArticle.setContent("New Content");
        updatedArticle.setUser(user);
        updatedArticle.setCategory(category);
        updatedArticle.setTags(Arrays.asList(tag1, tag2));

        when(articleRepository.findById(articleId)).thenReturn(Optional.of(existingArticle));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(tagRepository.findById(tag1.getId())).thenReturn(Optional.of(tag1));
        when(tagRepository.findById(tag2.getId())).thenReturn(Optional.of(tag2));
        when(articleRepository.save(existingArticle)).thenReturn(existingArticle);

        // Act
        Article result = articleService.updateArticle(articleId, updatedArticle);

        // Assert
        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        assertEquals("New Content", result.getContent());
        assertEquals(user, result.getUser());
        assertEquals(category, result.getCategory());
        assertEquals(2, result.getTags().size());
        verify(articleRepository, times(1)).save(existingArticle);
    }

    // Test for deleteArticle (Article Found)
    @Test
    public void testDeleteArticle() {
        // Arrange
        UUID id = UUID.randomUUID();
        Article article = new Article();
        when(articleRepository.findById(id)).thenReturn(Optional.of(article));

        // Act
        articleService.deleteArticle(id);

        // Assert
        verify(articleRepository, times(1)).deleteById(id);
    }

    // Test for deleteArticle (Article Not Found)
    @Test
    public void testDeleteArticle_NotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(articleRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ArticleNotFoundException.class, () -> articleService.deleteArticle(id));
        verify(articleRepository, never()).deleteById(id);
    }
}
package org.capps.news.service;

import org.capps.news.model.Article;
import org.capps.news.model.Comment;
import org.capps.news.model.User;
import org.capps.news.repository.ArticleRepository;
import org.capps.news.repository.CommentRepository;
import org.capps.news.repository.UserRepository;
import org.capps.news.web.exception.artilce.ArticleNotFoundException;
import org.capps.news.web.exception.comment.CommentNotFoundException;
import org.capps.news.web.exception.user.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public CommentService(CommentRepository commentRepository,
                          UserRepository userRepository,
                          ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    public Comment createComment(Comment comment) {
        User user = userRepository.findById(comment.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException("Comment not Found"));
        Article article = articleRepository.findById(comment.getArticle().getId())
                .orElseThrow(() -> new ArticleNotFoundException("Aritcle Not Found"));
        comment.setUser(user);
        comment.setArticle(article);
        return commentRepository.save(comment);
    }

    public Comment updateComment(Long id, Comment commentDetails) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));
        userRepository.findById(comment.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException("Comment not Found"));
        articleRepository.findById(comment.getArticle().getId())
                .orElseThrow(() -> new ArticleNotFoundException("Aritcle Not Found"));
        comment.setUsername(commentDetails.getUsername());
        comment.setEmail(commentDetails.getEmail());
        comment.setComments(commentDetails.getComments());
        return commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
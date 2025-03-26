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
import org.capps.news.web.vm.mapper.CommentVMMapper;
import org.capps.news.web.vm.request.CommentReqVM;
import org.capps.news.service.interfaces.CommentServiceInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService implements CommentServiceInterface {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CommentVMMapper commentVMMapper;

    public CommentService(CommentRepository commentRepository,
                          UserRepository userRepository,
                          ArticleRepository articleRepository,
                          CommentVMMapper commentVMMapper) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.commentVMMapper = commentVMMapper;
    }

    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public Comment createComment(CommentReqVM commentReqVM, String username) {
        User user = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Article article = articleRepository.findById(commentReqVM.getArticleId())
                .orElseThrow(() -> new ArticleNotFoundException("Article not found"));

        Comment comment = new Comment();
        comment.setComments(commentReqVM.getComments());
        comment.setUser(user);
        comment.setArticle(article);

        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(Long id, Comment commentDetails) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));
        userRepository.findById(comment.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException("Comment not Found"));
        articleRepository.findById(comment.getArticle().getId())
                .orElseThrow(() -> new ArticleNotFoundException("Article Not Found"));
        comment.setUsername(commentDetails.getUsername());
        comment.setEmail(commentDetails.getEmail());
        comment.setComments(commentDetails.getComments());
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}

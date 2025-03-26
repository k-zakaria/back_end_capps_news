package org.capps.news.service.interfaces;

import org.capps.news.model.Comment;
import org.capps.news.web.vm.request.CommentReqVM;

import java.util.List;
import java.util.Optional;

public interface CommentServiceInterface {

    List<Comment> getAllComments();

    Optional<Comment> getCommentById(Long id);

    Comment createComment(CommentReqVM commentReqVM, String username);

    Comment updateComment(Long id, Comment commentDetails);

    void deleteComment(Long id);
}

package org.capps.news.web.controller;

import lombok.AllArgsConstructor;
import org.capps.news.model.Comment;
import org.capps.news.service.CommentService;
import org.capps.news.web.vm.mapper.CommentVMMapper;
import org.capps.news.web.vm.request.CommentReqVM;
import org.capps.news.web.vm.response.CommentResVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final CommentVMMapper commentVMMapper;

    @Autowired
    public CommentController(CommentService commentService, CommentVMMapper commentVMMapper) {
        this.commentService = commentService;
        this.commentVMMapper = commentVMMapper;
    }

    @GetMapping
    public List<CommentResVM> getAllComments() {
        return commentService.getAllComments().stream()
                .map(commentVMMapper::commentToCommentResVM)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResVM> getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id)
                .map(commentVMMapper::commentToCommentResVM)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CommentResVM> createComment(
            @RequestBody CommentReqVM commentReqVM,
            @AuthenticationPrincipal UserDetails userDetails) { // Utilisateur authentifié
        String username = userDetails.getUsername(); // Récupérer le nom d'utilisateur
        Comment createdComment = commentService.createComment(commentReqVM, username);
        CommentResVM response = commentVMMapper.commentToCommentResVM(createdComment);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResVM> updateComment(@PathVariable Long id, @RequestBody CommentReqVM commentReqVM) {
        Comment comment = commentVMMapper.commentReqVMToComment(commentReqVM);
        Comment updatedComment = commentService.updateComment(id, comment);
        CommentResVM response = commentVMMapper.commentToCommentResVM(updatedComment);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
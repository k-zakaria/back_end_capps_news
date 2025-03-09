package org.capps.news.web.vm.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResVM {
    private Long id;
    private String username;
    private String email;
    private String comments;
    private Long userId;
    private UUID articleId;
}
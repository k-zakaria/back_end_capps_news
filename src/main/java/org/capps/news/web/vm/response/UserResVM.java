package org.capps.news.web.vm.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.capps.news.model.enums.Role;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResVM {
    private Long id;
    private String username;
    private String email;
    private String preferredLanguage;
    private boolean isVerified;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
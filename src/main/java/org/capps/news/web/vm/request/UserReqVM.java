package org.capps.news.web.vm.request;

import lombok.*;
import org.capps.news.model.enums.Role;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserReqVM {
    private Role role;
}

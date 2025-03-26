package org.capps.news.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import static org.capps.news.model.enums.Permission.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {
    USER(
            Set.of(
                    NEWS_CONTRIBUTOR_CREATE_ARTICLE,
                    NEWS_CONTRIBUTOR_EDIT_ARTICLE,
                    NEWS_CONTRIBUTOR_DELETE_ARTICLE,
                    NEWS_CONTRIBUTOR_PUBLISH_ARTICLE,
                    NEWS_CONTRIBUTOR_VIEW_OWN_ARTICLES,
                    NEWS_CONTRIBUTOR_UPLOAD_MEDIA,
                    NEWS_CONTRIBUTOR_MANAGE_MEDIA,
                    NEWS_CONTRIBUTOR_MANAGE_PROFILE,
                    NEWS_CONTRIBUTOR_CHANGE_PASSWORD,
                    NEWS_CONTRIBUTOR_MODERATE_COMMENTS,
                    NEWS_CONTRIBUTOR_MANAGE_CATEGORIES,
                    NEWS_CONTRIBUTOR_MANAGE_TAGS,
                    NEWS_CONTRIBUTOR_VIEW_ARTICLE_STATS
            )
    ),

    AUTHOR(
            Set.of(
                    NEWS_CONTRIBUTOR_CREATE_ARTICLE,
                    NEWS_CONTRIBUTOR_EDIT_ARTICLE,
                    NEWS_CONTRIBUTOR_DELETE_ARTICLE,
                    NEWS_CONTRIBUTOR_PUBLISH_ARTICLE,
                    NEWS_CONTRIBUTOR_VIEW_OWN_ARTICLES,
                    NEWS_CONTRIBUTOR_UPLOAD_MEDIA
            )
    ),

    ADMIN(
            Set.of(
                    ADMIN_VIEW_USERS,
                    ADMIN_MANAGE_USERS,
                    ADMIN_ASSIGN_ROLES,
                    ADMIN_VIEW_ALL_ARTICLES,
                    ADMIN_EDIT_ALL_ARTICLES,
                    ADMIN_DELETE_ALL_ARTICLES,
                    ADMIN_PUBLISH_ANY_ARTICLE,
                    ADMIN_MANAGE_MEDIA_LIBRARY,
                    ADMIN_MODERATE_ALL_COMMENTS ,
                    ADMIN_MANAGE_CATEGORIES,
                    ADMIN_MANAGE_TAGS,
                    ADMIN_MANAGE_TEMPLATES,
                    ADMIN_CUSTOMIZE_TEMPLATES,
                    ADMIN_MANAGE_SITE_SETTINGS,
                    ADMIN_MANAGE_SEO_SETTINGS,
                    ADMIN_MANAGE_ANALYTICS,
                    ADMIN_VIEW_SITE_STATS,
                    ADMIN_GENERATE_REPORTS,
                    ADMIN_MANAGE_SUBSCRIPTIONS,
                    ADMIN_MANAGE_PAYMENTS
            )
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities =getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + name()));
        return authorities;
    }
}
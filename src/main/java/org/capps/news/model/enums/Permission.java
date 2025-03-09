package org.capps.news.model.enums;

public enum Permission {
    // Permissions pour les Rédacteurs/Éditeurs
    NEWS_CONTRIBUTOR_CREATE_ARTICLE("news_contributor:create_article"),
    NEWS_CONTRIBUTOR_EDIT_ARTICLE("news_contributor:edit_article"),
    NEWS_CONTRIBUTOR_DELETE_ARTICLE("news_contributor:delete_article"),
    NEWS_CONTRIBUTOR_PUBLISH_ARTICLE("news_contributor:publish_article"),
    NEWS_CONTRIBUTOR_VIEW_OWN_ARTICLES("news_contributor:view_own_articles"),
    NEWS_CONTRIBUTOR_UPLOAD_MEDIA("news_contributor:upload_media"),
    NEWS_CONTRIBUTOR_MANAGE_MEDIA("news_contributor:manage_media"),
    NEWS_CONTRIBUTOR_MANAGE_PROFILE("news_contributor:manage_profile"),
    NEWS_CONTRIBUTOR_CHANGE_PASSWORD("news_contributor:change_password"),
    NEWS_CONTRIBUTOR_MODERATE_COMMENTS("news_contributor:moderate_comments"),
    NEWS_CONTRIBUTOR_MANAGE_CATEGORIES("news_contributor:manage_categories"),
    NEWS_CONTRIBUTOR_MANAGE_TAGS("news_contributor:manage_tags"),
    NEWS_CONTRIBUTOR_VIEW_ARTICLE_STATS("news_contributor:view_article_stats"),

    // Permissions pour les Administrateurs
    ADMIN_VIEW_USERS("admin:view_users"),
    ADMIN_MANAGE_USERS("admin:manage_users"),
    ADMIN_ASSIGN_ROLES("admin:assign_roles"),
    ADMIN_VIEW_ALL_ARTICLES("admin:view_all_articles"),
    ADMIN_EDIT_ALL_ARTICLES("admin:edit_all_articles"),
    ADMIN_DELETE_ALL_ARTICLES("admin:delete_all_articles"),
    ADMIN_PUBLISH_ANY_ARTICLE("admin:publish_any_article"),
    ADMIN_MANAGE_MEDIA_LIBRARY("admin:manage_media_library"),
    ADMIN_MODERATE_ALL_COMMENTS("admin:moderate_all_comments"),
    ADMIN_MANAGE_CATEGORIES("admin:manage_categories"),
    ADMIN_MANAGE_TAGS("admin:manage_tags"),
    ADMIN_MANAGE_TEMPLATES("admin:manage_templates"),
    ADMIN_CUSTOMIZE_TEMPLATES("admin:customize_templates"),
    ADMIN_MANAGE_SITE_SETTINGS("admin:manage_site_settings"),
    ADMIN_MANAGE_SEO_SETTINGS("admin:manage_seo_settings"),
    ADMIN_MANAGE_ANALYTICS("admin:manage_analytics"),
    ADMIN_VIEW_SITE_STATS("admin:view_site_stats"),
    ADMIN_GENERATE_REPORTS("admin:generate_reports"),
    ADMIN_MANAGE_SUBSCRIPTIONS("admin:manage_subscriptions"),
    ADMIN_MANAGE_PAYMENTS("admin:manage_payments");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}

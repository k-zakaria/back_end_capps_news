package org.capps.news.web.vm.mapper;

import org.capps.news.model.Article;
import org.capps.news.model.Category;
import org.capps.news.model.Tag;
import org.capps.news.web.vm.request.ArticleReqVM;
import org.capps.news.web.vm.response.ArticleResVM;
import org.capps.news.web.vm.response.TagSummaryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ArticleVMMapper {
    ArticleVMMapper INSTANCE = Mappers.getMapper(ArticleVMMapper.class);

    @Mapping(target = "author.id", source = "user.id")
    @Mapping(target = "author.username", source = "user.username")
    @Mapping(target = "category.id", source = "category.id")
    @Mapping(target = "category.name", source = "category.name")
    @Mapping(target = "tags", qualifiedByName = "mapTagsToTagSummary")
    ArticleResVM articleToArticleResVM(Article article);

    @Named("mapTagsToTagSummary")
    default List<TagSummaryDTO> mapTagsToTagSummary(List<Tag> tags) {
        if (tags == null) return Collections.emptyList();

        return tags.stream()
                .map(tag -> new TagSummaryDTO(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
    }

    @Mapping(target = "category.id", source = "categoryId")
    @Mapping(target = "tags", source = "tagIds")
    Article articleReqVMToArticle(ArticleReqVM articleReqVM);

    // Méthode de conversion pour les tags
    default List<Tag> mapTagIds(List<Long> tagIds) {
        if (tagIds == null) return new ArrayList<>();

        return tagIds.stream()
                .map(tagId -> {
                    Tag tag = new Tag();
                    tag.setId(tagId);
                    return tag;
                })
                .collect(Collectors.toList());
    }

    // Méthode de conversion pour la catégorie
    default Category mapCategoryId(Long categoryId) {
        if (categoryId == null) return null;

        Category category = new Category();
        category.setId(categoryId);
        return category;
    }
}
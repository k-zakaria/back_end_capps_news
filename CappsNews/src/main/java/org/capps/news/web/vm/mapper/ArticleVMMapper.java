package org.capps.news.web.vm.mapper;

import org.capps.news.model.Article;
import org.capps.news.web.vm.request.ArticleReqVM;
import org.capps.news.web.vm.response.ArticleResVM;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ArticleVMMapper {
    ArticleVMMapper INSTANCE = Mappers.getMapper(ArticleVMMapper.class);

    ArticleResVM articleToArticleResVM(Article article);

    Article articleReqVMToArticle(ArticleReqVM articleReqVM);
}
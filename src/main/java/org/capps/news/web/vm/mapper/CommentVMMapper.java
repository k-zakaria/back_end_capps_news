package org.capps.news.web.vm.mapper;

import org.capps.news.model.Comment;
import org.capps.news.web.vm.request.CommentReqVM;
import org.capps.news.web.vm.response.CommentResVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentVMMapper {
    CommentVMMapper INSTANCE = Mappers.getMapper(CommentVMMapper.class);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "article", ignore = true)
    Comment commentReqVMToComment(CommentReqVM commentReqVM);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "articleId", source = "article.id")
    CommentResVM commentToCommentResVM(Comment comment);
}
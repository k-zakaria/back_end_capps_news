package org.capps.news.web.vm.mapper;

import org.capps.news.model.Tag;
import org.capps.news.web.vm.request.TagReqVM;
import org.capps.news.web.vm.response.TagResVM;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TagVMMapper {
    TagVMMapper INSTANCE = Mappers.getMapper(TagVMMapper.class);

    Tag tagReqVMToTag(TagReqVM tagReqVM);

    TagResVM tagToTagResVM(Tag tag);
}
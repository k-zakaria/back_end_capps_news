package org.capps.news.web.vm.mapper;

import org.capps.news.model.Category;
import org.capps.news.web.vm.request.CategoryReqVM;
import org.capps.news.web.vm.response.CategoryResVM;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryVMMapper {
    CategoryVMMapper INSTANCE = Mappers.getMapper(CategoryVMMapper.class);

    Category categoryReqVMToCategory(CategoryReqVM categoryReqVM);

    CategoryResVM categoryToCategoryResVM(Category category);
}
package org.capps.news.web.vm.mapper;


import org.mapstruct.Mapper;
import org.capps.news.model.User;
import org.capps.news.web.vm.request.RegisterVM;

@Mapper(componentModel = "spring")
public interface UserVMMapper {
    User registerVMtoUser(RegisterVM registerVM);
}

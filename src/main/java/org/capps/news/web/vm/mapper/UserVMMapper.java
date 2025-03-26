package org.capps.news.web.vm.mapper;


import org.capps.news.web.vm.response.UserResVM;
import org.mapstruct.Mapper;
import org.capps.news.model.User;
import org.capps.news.web.vm.request.RegisterVM;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserVMMapper {
    User registerVMtoUser(RegisterVM registerVM);

    @Mapping(target = "role", source = "role")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    UserResVM userToUserResVM(User user);
}

package org.capps.news.service.interfaces;

import org.capps.news.model.User;
import org.capps.news.web.vm.request.UserReqVM;

import java.util.List;
import java.util.Optional;

public interface UserServiceInterface {

    Optional<User> findByUsername(String userName);

    Optional<User> findByEmail(String email);

    List<User> getAllUsers();

    User updateUserRole(Long userId, UserReqVM userReqVM);

    void deleteUser(Long userId);

    void permanentDeleteUser(Long userId);
}

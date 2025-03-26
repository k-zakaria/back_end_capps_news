package org.capps.news.web.controller;

import lombok.RequiredArgsConstructor;
import org.capps.news.model.User;
import org.capps.news.repository.UserRepository;
import org.capps.news.service.UserService;
import org.capps.news.service.interfaces.UserServiceInterface;
import org.capps.news.web.vm.mapper.UserVMMapper;
import org.capps.news.web.vm.request.UserReqVM;
import org.capps.news.web.vm.response.UserResVM;
import org.hibernate.query.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceInterface userService;
    private final UserVMMapper userVMMapper;
    private final UserRepository userRepository;



    @GetMapping
    public ResponseEntity<List<UserResVM>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        List<UserResVM> userResVMs = users.stream()
                .map(userVMMapper::userToUserResVM)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userResVMs);
    }

    @PutMapping("/{userId}/role")
    public ResponseEntity<UserResVM> updateUserRole(
            @PathVariable Long userId,
            @RequestBody UserReqVM userReqVM
    ) {
        User updatedUser = userService.updateUserRole(userId, userReqVM);
        UserResVM userResVM = userVMMapper.userToUserResVM(updatedUser);
        return ResponseEntity.ok(userResVM);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
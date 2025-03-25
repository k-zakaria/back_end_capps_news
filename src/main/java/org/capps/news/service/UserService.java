package org.capps.news.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.capps.news.model.User;
import org.capps.news.model.enums.Role;
import org.capps.news.repository.UserRepository;
import org.capps.news.service.interfaces.UserServiceInterface;
import org.capps.news.web.exception.user.UnauthorizedRoleUpdateException;
import org.capps.news.web.exception.user.UserNotFoundException;
import org.capps.news.web.vm.request.UserReqVM;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findByUsername(String userName) {
        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        return userRepository.findByUsernameAndDeletedFalse(userName);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        return userRepository.findByEmailAndDeletedFalse(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAllByDeletedFalse();
    }

    @Override
    public User updateUserRole(Long userId, UserReqVM userReqVM) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        validateRoleUpdatePermissions(authentication);

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'ID: " + userId));

        existingUser.setRole(userReqVM.getRole());
        existingUser.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(existingUser);
    }

    private void validateRoleUpdatePermissions(Authentication authentication) {
        User currentUser = userRepository.findByUsernameAndDeletedFalse(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("Utilisateur connecté non trouvé"));

        if (currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedRoleUpdateException("Seul un administrateur peut modifier les rôles");
        }
    }

    @Override
    public void deleteUser(Long userId) {
        User userToDelete = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'ID: " + userId));

        userToDelete.setDeleted(true);
        userToDelete.setUpdatedAt(LocalDateTime.now());

        userRepository.save(userToDelete);
    }

    @Override
    public void permanentDeleteUser(Long userId) {
        User userToDelete = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'ID: " + userId));

        userRepository.delete(userToDelete);
    }
}

package org.capps.news.repository;

import org.capps.news.model.User;
import org.capps.news.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameAndDeletedFalse(String username);

    Optional<User> findByEmailAndDeletedFalse(String email);


    Optional<User> findByVerificationToken(String token);

    Optional<User> findByPasswordResetToken(String token);

    List<User> findAllByVerificationToken(String token);

    List<User> findAllByPasswordResetToken(String token);

    List<User> findAllByDeletedFalse();



}


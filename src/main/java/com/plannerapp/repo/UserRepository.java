package com.plannerapp.repo;

import com.plannerapp.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String value);


    Optional<User> getUserByUsername(String value);

    Set<User> findAllByIdNot(Long id);
}

package org.example.simpleonlinestore.repository;

import org.example.simpleonlinestore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmailId(String email);

}

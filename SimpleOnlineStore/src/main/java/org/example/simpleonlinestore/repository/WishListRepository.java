package org.example.simpleonlinestore.repository;

import org.example.simpleonlinestore.entity.User;
import org.example.simpleonlinestore.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList,Long> {
    Optional<WishList> findByUser(User user);
}

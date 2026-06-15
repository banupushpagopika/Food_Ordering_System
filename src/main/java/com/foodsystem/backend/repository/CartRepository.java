package com.foodsystem.backend.repository;

import com.foodsystem.backend.entity.Cart;
import com.foodsystem.backend.entity.MenuItem;
import com.foodsystem.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUser(User user);
    Optional<Cart> findByUserAndMenuItem(User user, MenuItem menuItem);
    void deleteByUser(User user);
}

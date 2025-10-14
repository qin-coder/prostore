package com.xuwei.prostore.repository;


import com.xuwei.prostore.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}

package com.teamvoy.shop.repository;

import com.teamvoy.shop.entity.Basket;
import com.teamvoy.shop.entity.BasketPhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {

    @Query(value = "SELECT * FROM baskets WHERE user_id = :userId", nativeQuery = true)
    Optional<Basket> getByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM basket_phones WHERE phone_id = :phoneId", nativeQuery = true)
    Optional<BasketPhone> getBasketPhone(@Param("phoneId") Long phoneId);
}

package com.teamvoy.shop.repository;

import com.teamvoy.shop.entity.BasketPhone;
import com.teamvoy.shop.entity.BasketPhonePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketPhoneRepository extends JpaRepository<BasketPhone, BasketPhonePK> {
}

package com.teamvoy.shop.repository;

import com.teamvoy.shop.entity.OrderPhone;
import com.teamvoy.shop.entity.OrderPhonePK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPhoneRepository extends JpaRepository<OrderPhone, OrderPhonePK> {
}

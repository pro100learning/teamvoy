package com.teamvoy.shop.repository;

import com.teamvoy.shop.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {

    @Query(value = "SELECT COUNT(*) FROM phones", nativeQuery = true)
    long getCount();
}

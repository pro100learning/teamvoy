package com.teamvoy.shop.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "baskets")
@Builder(toBuilder = true)
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "basket", fetch = FetchType.LAZY, targetEntity = BasketPhone.class)
    private List<BasketPhone> phones = new ArrayList<>();

    @Column(name = "price", nullable = false)
    private double price;
}

package com.teamvoy.shop.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "baskets")
@Builder(toBuilder = true)
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Phone.class)
    @JoinTable(
            name = "basket_phones",
            joinColumns = @JoinColumn(name = "basket_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "phone_id", referencedColumnName = "id")
    )
    private Set<Phone> phones;

    @Column(name = "price", nullable = false)
    private double price;
}

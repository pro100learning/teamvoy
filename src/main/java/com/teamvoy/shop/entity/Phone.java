package com.teamvoy.shop.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "phones")
@Builder(toBuilder = true)
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "color")
    private String color;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "phones", fetch = FetchType.LAZY, targetEntity = Basket.class)
    private Set<Basket> baskets;

    @ManyToMany(mappedBy = "phones", fetch = FetchType.LAZY, targetEntity = Order.class)
    private Set<Order> orders;
}

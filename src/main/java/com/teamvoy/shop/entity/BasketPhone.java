package com.teamvoy.shop.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "basket_phones")
public class BasketPhone {

    @EmbeddedId
    private BasketPhonePK id;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Basket.class)
    @MapsId("basketId")
    @JoinColumn(name = "basket_id", referencedColumnName = "id")
    private Basket basket;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Phone.class)
    @MapsId("phoneId")
    @JoinColumn(name = "phone_id", referencedColumnName = "id")
    private Phone phone;

    @Column(name = "amount", nullable = false)
    private int amount;
}

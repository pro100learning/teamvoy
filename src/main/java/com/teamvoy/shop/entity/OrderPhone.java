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
@Table(name = "order_phones")
public class OrderPhone {

    @EmbeddedId
    private OrderPhonePK id;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Order.class)
    @MapsId("orderId")
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Phone.class)
    @MapsId("phoneId")
    @JoinColumn(name = "phone_id", referencedColumnName = "id")
    private Phone phone;

    @Column(name = "amount", nullable = false)
    private int amount;
}

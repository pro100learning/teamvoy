package com.teamvoy.shop.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class OrderPhonePK implements Serializable {

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "phone_id", nullable = false)
    private Long phoneId;
}

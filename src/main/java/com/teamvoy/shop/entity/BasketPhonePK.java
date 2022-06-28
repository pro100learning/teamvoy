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
public class BasketPhonePK implements Serializable {

    @Column(name = "basket_id", nullable = false)
    private Long basketId;

    @Column(name = "phone_id", nullable = false)
    private Long phoneId;
}

package com.teamvoy.shop.entity;

import com.teamvoy.shop.entity.enums.Role;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Builder(toBuilder = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "password", length = 64, nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER, targetClass = Role.class)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private List<Role> roles;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;


    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, targetEntity = Basket.class)
    private Basket basket;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, targetEntity = Order.class)
    private List<Order> orders;
}

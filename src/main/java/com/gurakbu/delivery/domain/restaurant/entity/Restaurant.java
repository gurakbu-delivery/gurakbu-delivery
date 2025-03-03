package com.gurakbu.delivery.domain.restaurant.entity;

import com.gurakbu.delivery.common.BaseTimeEntity;
import com.gurakbu.delivery.domain.restaurant.enums.RestaurantCategory;
import com.gurakbu.delivery.domain.restaurant.enums.RestaurantStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Entity
@Table(name = "restaurants")
@NoArgsConstructor
public class Restaurant extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column
    private String description;

    @Column(nullable = false)
    private RestaurantCategory category;

    @Column(nullable = false)
    private RestaurantStatus status;

    @Column
    private LocalTime openTime;

    @Column
    private LocalTime closeTime;

    @Column
    private Integer minDeliveryPrice;

    @Column
    private LocalDateTime deletedAt;

    public Restaurant(String name, String address, String description, RestaurantCategory category, RestaurantStatus status, LocalTime openTime, LocalTime closeTime, Integer minDeliveryPrice) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.category = category;
        this.status = status;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.minDeliveryPrice = minDeliveryPrice;
    }
}

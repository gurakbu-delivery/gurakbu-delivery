package com.gurakbu.delivery.domain.restaurant.entity;

import com.gurakbu.delivery.common.BaseTimeEntity;
import com.gurakbu.delivery.domain.restaurant.dto.request.RestaurantUpdateRequestDto;
import com.gurakbu.delivery.domain.restaurant.enums.RestaurantCategory;
import com.gurakbu.delivery.domain.restaurant.enums.RestaurantStatus;
import com.gurakbu.delivery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Entity
@Table(name = "restaurants")
@NoArgsConstructor
public class Restaurant extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RestaurantCategory category;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RestaurantStatus status;

    @Column
    private LocalTime openTime;

    @Column
    private LocalTime closeTime;

    @Column
    private Integer minDeliveryPrice;

    public Restaurant(User user, String name, String address, String description, RestaurantCategory category, RestaurantStatus status, LocalTime openTime, LocalTime closeTime, Integer minDeliveryPrice) {
        this.user = user;
        this.name = name;
        this.address = address;
        this.description = description;
        this.category = category;
        this.status = status;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.minDeliveryPrice = minDeliveryPrice;
    }

    public void updateRestaurant(User user, RestaurantUpdateRequestDto dto){
        this.user = user;
        if(dto.getName() != null){
            this.name = dto.getName();
        }
        if(dto.getAddress() != null){
            this.address = dto.getAddress();
        }
        if(dto.getDescription() != null){
            this.description = dto.getDescription();
        }
        if(dto.getCategory() != null){
            this.category = dto.getCategory();
        }
        if(dto.getOpenTime() != null){
            this.openTime = dto.getOpenTime();
        }
        if(dto.getCloseTime() != null){
            this.closeTime = dto.getCloseTime();
        }
        if(dto.getMinDeliveryPrice() != null){
            this.minDeliveryPrice = dto.getMinDeliveryPrice();
        }
    }

    public void close(){
        this.status = RestaurantStatus.CLOSED;
    }

}
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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column
    private String description;

    @Enumerated(EnumType.STRING) // 문자열로 저장됨 ("KOREAN", "JAPANESE", ...)
    @Column(nullable = false)
    private RestaurantCategory category;

    @Enumerated(EnumType.STRING) // 상태도 문자열로 저장
    @Column(nullable = false)
    private RestaurantStatus status;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Column(nullable = false)
    private Integer minDeliveryPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

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

    public void updateRestaurant(RestaurantUpdateRequestDto dto){
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

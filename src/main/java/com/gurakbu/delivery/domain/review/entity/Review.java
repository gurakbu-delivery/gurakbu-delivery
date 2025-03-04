package com.gurakbu.delivery.domain.review.entity;

import com.gurakbu.delivery.common.BaseTimeEntity;
import com.gurakbu.delivery.domain.menu.entity.Menu;
import com.gurakbu.delivery.domain.order.entity.Order;
import com.gurakbu.delivery.domain.restaurant.entity.Restaurant;
import com.gurakbu.delivery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "reviews")
public class Review extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Builder
    public Review(Integer rating, String contents, User user, Restaurant restaurant, Menu menu, Order order) {
        this.rating = rating;
        this.contents = contents;
        this.user = user;
        this.restaurant = restaurant;
        this.menu = menu;
        this.order = order;
    }

}


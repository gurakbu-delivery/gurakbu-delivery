package com.gurakbu.delivery.domain.review.entity;

import com.gurakbu.delivery.common.BaseTimeEntity;
import com.gurakbu.delivery.domain.menu.entity.Menu;
import com.gurakbu.delivery.domain.order.entity.Order;
import com.gurakbu.delivery.domain.restaurant.entity.Restaurant;
import com.gurakbu.delivery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
    @JoinColumn(name = "order_id")
    private Order order;

    // 사용자가 남긴 리뷰 ( 상위 리뷰 : 부모 리뷰로 관리)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_review_id")
    private Review parentReview;

    // 리뷰에 대한 사장님의 답변 ( 하위 리뷰 : 자식 리뷰로 관리)
    @OneToMany(mappedBy = "parentReview",cascade = CascadeType.ALL)
    private List<Review> childReviews = new ArrayList<>();


    @Builder
    public Review(Integer rating, String contents, User user, Restaurant restaurant, Order order) {
        this.rating = rating;
        this.contents = contents;
        this.user = user;
        this.restaurant = restaurant;
        this.order = order;
    }

}


package com.gurakbu.delivery.domain.menu.entity;

import com.gurakbu.delivery.common.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "menus")
public class Menu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}

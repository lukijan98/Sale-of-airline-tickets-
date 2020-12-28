package com.lal.flightservice.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Airplane {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int capacity;
}

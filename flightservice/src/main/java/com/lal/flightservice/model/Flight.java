package com.lal.flightservice.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "AIRPLANE", referencedColumnName = "ID")
    private Airplane airplane;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private int miles;

    @Column(nullable = false)
    private int price;
}

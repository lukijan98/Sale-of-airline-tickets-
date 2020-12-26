package com.lal.userservice.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class CreditCard {
    @Id
    @Column(unique = true,nullable = false)
    private String cardNumber;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false)
    private String ownerLastname;

    @Column(nullable = false)
    private int securityNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;
}

package com.lal.userservice.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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

    @ManyToOne
    private User user;
}

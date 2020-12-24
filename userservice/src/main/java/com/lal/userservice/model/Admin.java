package com.lal.userservice.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Admin {

    @Id
    @Column(nullable = false,unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
}

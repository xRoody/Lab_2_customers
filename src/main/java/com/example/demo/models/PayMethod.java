package com.example.demo.models;

import javax.persistence.*;

@Entity
@Table(name = "pay_method")
public class PayMethod {
    @Id
    @Column(unique = true, nullable = false, name = "pay_name")
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;
}

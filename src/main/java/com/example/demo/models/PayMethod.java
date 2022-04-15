package com.example.demo.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "pay_method")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PayMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "data")
    private String data;
}

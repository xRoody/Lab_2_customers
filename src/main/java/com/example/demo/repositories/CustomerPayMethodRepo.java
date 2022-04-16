package com.example.demo.repositories;

import com.example.demo.models.CustomerPayMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerPayMethodRepo extends JpaRepository<CustomerPayMethod,Long> {
}

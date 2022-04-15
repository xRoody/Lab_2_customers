package com.example.demo.repositories;

import com.example.demo.models.PayMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayMethodRepo extends JpaRepository<PayMethod, Long> {
}

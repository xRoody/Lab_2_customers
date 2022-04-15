package com.example.demo.repositories;

import com.example.demo.models.CurPayMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurPayMethodRepo extends JpaRepository<CurPayMethod,Long> {
}

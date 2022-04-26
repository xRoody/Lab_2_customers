package com.example.demo;

import com.example.demo.DTOs.AddressDTO;
import com.example.demo.repositories.AddressRepo;
import com.example.demo.repositories.CustomerRepo;
import com.example.demo.services.AddressService;
import com.example.demo.services.CustomerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        ApplicationContext context=SpringApplication.run(DemoApplication.class, args);
    }
}

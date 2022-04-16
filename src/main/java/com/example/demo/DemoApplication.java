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
        CustomerRepo customerRepo=context.getBean(CustomerRepo.class);
        CustomerService customerService=context.getBean(CustomerService.class);
        AddressRepo addressRepo=context.getBean(AddressRepo.class);
        AddressService addressService=context.getBean(AddressService.class);
        //addressRepo.save(addressService.getByDTO(new AddressDTO(null, "tet", "tet", "tet", 10, 1l)));
        System.out.println(addressRepo.findAll());
        System.out.println(customerService.findDTOById(1l));
    }
}

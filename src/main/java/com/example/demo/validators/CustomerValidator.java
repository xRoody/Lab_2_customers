package com.example.demo.validators;

import com.example.demo.DTOs.CustomerDTO;
import com.example.demo.exceptions.BodyReport;

import java.util.List;

public interface CustomerValidator {
     List<BodyReport> validateCustomerAddDTO(CustomerDTO customerDTO);
     List<BodyReport> validateCustomerUpdateDTO(CustomerDTO customerDTO, Long id);
}

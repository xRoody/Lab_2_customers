package com.example.demo.services;

import com.example.demo.DTOs.CustomerPayMethodDTO;
import com.example.demo.models.CustomerPayMethod;

import java.util.List;

public interface CustomerPayMethodService {
    CustomerPayMethod getByDTO(CustomerPayMethodDTO dto);
    CustomerPayMethodDTO getDTObyObject(CustomerPayMethod customerPayMethod);
    CustomerPayMethodDTO getDTOById(Long id);
    List<CustomerPayMethodDTO> getAllCurPayMethods();
    void addNewCurPayMethod(CustomerPayMethodDTO customerPayMethodDTO);
    boolean deleteCurPayMethod(Long id);
    void updateCurPayMethod(CustomerPayMethodDTO dto, Long id);
}

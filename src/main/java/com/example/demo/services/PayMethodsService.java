package com.example.demo.services;


import com.example.demo.DTOs.PayMethodDTO;

import java.util.List;

public interface PayMethodsService {
    void addNewPayMethod(PayMethodDTO payMethodDTO);
    boolean deletePayMethod(Long id);
    PayMethodDTO getDTOById(Long id);
    List<PayMethodDTO> getAllDTOs();
    void updatePayMethod(PayMethodDTO payMethodDTO, Long id);
    Boolean isExists(Long id);
}

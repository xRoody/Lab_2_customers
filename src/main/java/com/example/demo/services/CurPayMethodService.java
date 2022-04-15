package com.example.demo.services;

import com.example.demo.DTOs.CurPayMethodDTO;
import com.example.demo.models.CurPayMethod;

import java.util.List;

public interface CurPayMethodService {
    CurPayMethod getByDTO(CurPayMethodDTO dto);
    CurPayMethodDTO getDTObyObject(CurPayMethod curPayMethod);
    CurPayMethodDTO getDTOById(Long id);
    List<CurPayMethodDTO> getAllCurPayMethods();
    void addNewCurPayMethod(CurPayMethodDTO curPayMethodDTO);
    boolean deleteCurPayMethod(Long id);
    void updateCurPayMethod(CurPayMethodDTO dto, Long id);
}

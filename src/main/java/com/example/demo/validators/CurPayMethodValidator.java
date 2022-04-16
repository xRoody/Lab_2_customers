package com.example.demo.validators;

import com.example.demo.DTOs.CustomerPayMethodDTO;
import com.example.demo.exceptions.BodyReport;

import java.util.List;

public interface CurPayMethodValidator {
    List<BodyReport> validateNewCurPayMethod(CustomerPayMethodDTO customerPayMethodDTO);
}

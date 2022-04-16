package com.example.demo.validators;

import com.example.demo.DTOs.PayMethodDTO;
import com.example.demo.exceptions.BodyReport;

import java.util.List;

public interface PayMethodValidator {
    List<BodyReport> validatePayMethod(PayMethodDTO payMethodDTO);
}

package com.example.demo.validators;

import com.example.demo.DTOs.PayMethodDTO;
import com.example.demo.exceptions.BodyReport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PayMethodValidatorImpl implements PayMethodValidator{

    @Override
    public List<BodyReport> validatePayMethod(PayMethodDTO payMethodDTO) {
        List<BodyReport> reports=new ArrayList<>();
        validateData(payMethodDTO.getData(), reports);
        validateTitle(payMethodDTO.getTitle(), reports);
        return reports;
    }

    private void validateTitle(String title, List<BodyReport> reports){
        if (title==null || title.isBlank()) reports.add(new BodyReport("e-001", "title", "Title must be not empty"));
    }

    private void validateData(String data, List<BodyReport> reports){
        if (data==null || data.isBlank()) reports.add(new BodyReport("e-001", "data", "Data must be not empty"));
    }
}

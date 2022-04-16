package com.example.demo.validators;

import com.example.demo.DTOs.CustomerPayMethodDTO;
import com.example.demo.exceptions.BodyReport;
import com.example.demo.services.CustomerService;
import com.example.demo.services.PayMethodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class CurPayMethodValidatorImpl implements CurPayMethodValidator {
    private final CustomerService customerService;
    private final PayMethodsService payMethodsService;
    private final static Pattern TITLE_PATTERN=Pattern.compile("[a-zA-Z\\d\\s]*");

    public List<BodyReport> validateNewCurPayMethod(CustomerPayMethodDTO customerPayMethodDTO){
        List<BodyReport> bodyReports=new ArrayList<>();
        validateTitle(customerPayMethodDTO.getName(), bodyReports);
        validateData(customerPayMethodDTO.getData(), bodyReports);
        validateCustomerId(customerPayMethodDTO.getCustomerId(),bodyReports);
        validatePayMethodId(customerPayMethodDTO.getPayId(),bodyReports);
        return bodyReports;
    }

    private void validateTitle(String title,List<BodyReport> bodyReports){
        if (title==null || title.isBlank()) bodyReports.add( new BodyReport("e-001", "title", "Title must be not null/empty"));
        else if (!TITLE_PATTERN.matcher(title).matches()) bodyReports.add(new BodyReport("e-002", "title", "Incorrect title format"));
    }

    private void validateData(String data, List<BodyReport> bodyReports){
        if (data==null || data.isBlank()) bodyReports.add(new BodyReport("e-001", "data", "Data must be not null/empty"));
    }

    private void validateCustomerId(Long id, List<BodyReport> bodyReports){
        if(!customerService.isExists(id)) bodyReports.add(new BodyReport("e-003", "customerId", "Incorrect customer Id state. Customer is not exists"));
    }

    private void validatePayMethodId(Long id, List<BodyReport> bodyReports){
        if(!payMethodsService.isExists(id)) bodyReports.add(new BodyReport("e-003", "payId", "Incorrect pay method id state. Pay method is not exists"));
    }
}

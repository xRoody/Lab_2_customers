package com.example.demo.validators;

import com.example.demo.DTOs.AddressDTO;
import com.example.demo.DTOs.CustomerDTO;
import com.example.demo.DTOs.CustomerPayMethodDTO;
import com.example.demo.exceptions.BodyReport;
import com.example.demo.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class CustomerValidatorImpl implements CustomerValidator {
    private final CustomerService customerService;
    //@Value()
    private static final int OLD_LIMIT = 120;
    //@Value()
    private static final int YOUNG_LIMIT = 14;
    private static final Pattern loginPattern = Pattern.compile("[a-zA-Z\\d]([A-Za-z\\d]|[#@$!%?&])*[a-zA-Z\\d]");
    private static final Pattern passwordPattern = Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#@$!%?&])[A-za-z\\d#@$!%?&]*");
    private static final Pattern emailPattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])");

    public List<BodyReport> validateCustomerAddDTO(CustomerDTO customerDTO) {
        List<BodyReport> reports = new ArrayList<>();
        //validateLogin
        validateString(customerDTO.getLogin(), loginPattern, "login", true, reports);
        //validatePassword
        validateString(customerDTO.getPassword(), passwordPattern, "password", false, reports);
        //validateEmail
        validateString(customerDTO.getEmail(), emailPattern, "email", true, reports);
        validateDob(customerDTO.getDob(), reports);
        return reports;
    }

    public List<BodyReport> validateCustomerUpdateDTO(CustomerDTO customerDTO, Long id) {
        List<BodyReport> reports = new ArrayList<>();
        //validate Login
        validateString(customerDTO.getLogin(), loginPattern, "login", false, reports);
        //validate Password
        validateString(customerDTO.getPassword(), passwordPattern, "password", false, reports);
        //validate Email
        validateString(customerDTO.getEmail(), emailPattern, "email", false, reports);
        //validate Dob
        validateDob(customerDTO.getDob(), reports);
        //validate DTOs lists
        List<CustomerPayMethodDTO> customerPayMethodDTOS = new ArrayList<>(customerDTO.getPayMethods());
        validatePayMethodsDTOList(customerPayMethodDTOS, id, reports);
        List<AddressDTO> addressDTOS = new ArrayList<>(customerDTO.getAddresses());
        validateAddressDTOList(addressDTOS, id, reports);
        return reports;
    }

    private void validatePayMethodsDTOList(List<CustomerPayMethodDTO> customerPayMethodDTOS, Long id, List<BodyReport> reports) {
        for (int i = 0; i < customerPayMethodDTOS.size(); i++) {
            if (customerPayMethodDTOS.get(i).getCustomerId() == null)
                reports.add(new BodyReport("e-001", "payMethods[" + i + "]", "payMethods[" + i + "].customerId must be not null"));
            else if (!customerPayMethodDTOS.get(i).getCustomerId().equals(id))
                reports.add(new BodyReport("e-002", "payMethods[" + i + "]", "payMethods[" + i + "].customerId incorrect. It must equals customer.id"));
        }
    }

    private void validateAddressDTOList(List<AddressDTO> addressDTOS, Long id, List<BodyReport> reports) {
        for (int i = 0; i < addressDTOS.size(); i++) {
            if (addressDTOS.get(i).getCustomerId() == null)
                reports.add(new BodyReport("e-001", "address[" + i + "]", "address[" + i + "].customerId must be not null"));
            else if (!addressDTOS.get(i).getCustomerId().equals(id))
                reports.add(new BodyReport("e-002", "address[" + i + "]", "address[" + i + "].customerId incorrect. It must equals customer.id"));
        }
    }

    private void validateString(String str, Pattern pattern, String fieldName, boolean isUnique, List<BodyReport> reports) {
        if (str == null || str.isBlank())
            reports.add(new BodyReport("e-001", fieldName, fieldName + " must be not empty (only whitespaces / null)"));
        else if (!pattern.matcher(str).matches())
            reports.add(new BodyReport("e-002", fieldName, "Incorrect " + fieldName + " format"));
        if (isUnique) checkIsUniqueValueExistsByFieldName(str, fieldName, reports);
    }

    private void checkIsUniqueValueExistsByFieldName(String str, String fieldName, List<BodyReport> reports) {
        Method method = Arrays.stream(customerService.getClass().getMethods())
                .filter(x -> x.getName().equals("is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1) + "Exists") && x.getParameterCount() == 1)
                .findFirst().orElse(null);
        if (method != null) {
            try {
                if ((Boolean) method.invoke(customerService, str))
                    reports.add(new BodyReport("e-003", fieldName, fieldName + " is already exists. This parameter should be unique"));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e.getCause());
            }
        }
    }


    private void validateDob(LocalDateTime date, List<BodyReport> reports) {
        if (date == null)
            reports.add(new BodyReport("e-001", "dob", "Dob must be not empty (only whitespaces / null)"));
        else {
            if (!date.isAfter(LocalDateTime.now().minusYears(OLD_LIMIT)))
                reports.add(new BodyReport("t-001", "dob", "Incorrect dob. Customer must be younger " + OLD_LIMIT));
            if (!date.isBefore(LocalDateTime.now().minusYears(YOUNG_LIMIT)))
                reports.add(new BodyReport("t-002", "dob", "Incorrect dob. Customer must be older " + YOUNG_LIMIT));
        }
    }
}

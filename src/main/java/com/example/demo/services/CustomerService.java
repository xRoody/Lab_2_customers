package com.example.demo.services;

import com.example.demo.DTOs.CustomerDTO;
import com.example.demo.DTOs.LoginFormDTO;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.models.Customer;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CustomerService extends UserDetailsService {
    CustomerDTO findDTOById(Long id);
    void addNewCustomer(CustomerDTO dto);
    Customer getById(Long id);
    boolean removeById(Long id);
    List<CustomerDTO> getAllCustomerDTOs();
    void update(CustomerDTO customerDTO, Long id) throws CustomerNotFoundException;
    Boolean isEmailExists(String email);
    Boolean isLoginExists(String login);
    Boolean isExists(Long id);
    CustomerDTO findDTOByEmail(String email);
    Object findOffers(Long id);
}

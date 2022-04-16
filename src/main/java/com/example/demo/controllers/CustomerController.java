package com.example.demo.controllers;

import com.example.demo.DTOs.CustomerDTO;
import com.example.demo.DTOs.CustomerPayMethodDTO;
import com.example.demo.exceptions.BodyReport;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.services.CustomerService;
import com.example.demo.validators.CustomerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.List;
/*
 * @TODO: add validation with response statuses
 * */

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerValidator customerValidator;

    @GetMapping("/{id}")
    public CustomerDTO getCustomerById(@PathVariable Long id) throws CustomerNotFoundException {
        CustomerDTO dto = customerService.findDTOById(id);
        if (dto == null)
            throw new CustomerNotFoundException(id);
        return dto;
    }

    @GetMapping()
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomerDTOs();
    }

    @PostMapping()
    public ResponseEntity<Object> addNewPerson(@RequestBody CustomerDTO customerDTO) {
        List<BodyReport> reports=customerValidator.validateCustomerAddDTO(customerDTO);
        if (reports.size()!=0){
            return new ResponseEntity<>(reports, HttpStatus.BAD_REQUEST);
        }
        customerService.addNewCustomer(customerDTO);
        return ResponseEntity.created(URI.create("/customers")).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCustomer(@PathVariable Long id) {
        boolean f=customerService.removeById(id);
        if (!f) return ResponseEntity.noContent().build();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object>  editCustomer(@RequestBody CustomerDTO customerDTO, @PathVariable("id") Long id) throws CustomerNotFoundException {
        List<BodyReport> reports=customerValidator.validateCustomerUpdateDTO(customerDTO, id);
        if (reports.size()!=0) return new ResponseEntity<>(reports, HttpStatus.CONFLICT);
        customerService.update(customerDTO, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/payMethods")
    public Collection<CustomerPayMethodDTO> getAllPMByCustomerId(@PathVariable("id") Long id) throws CustomerNotFoundException {
        CustomerDTO dto = customerService.findDTOById(id);
        if (dto==null) throw new CustomerNotFoundException(id);
        return dto.getPayMethods();
    }
}

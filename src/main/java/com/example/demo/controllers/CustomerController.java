package com.example.demo.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.DTOs.CustomerDTO;
import com.example.demo.DTOs.CustomerPayMethodDTO;
import com.example.demo.exceptions.BodyExceptionWrapper;
import com.example.demo.exceptions.BodyReport;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.filters.CustomFilter;
import com.example.demo.services.CustomerService;
import com.example.demo.validators.CustomerValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/*
 * @TODO: add validation with response statuses
 * */

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
@Slf4j
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerValidator customerValidator;

    @GetMapping("/{id}")
    public CustomerDTO getCustomerById(@PathVariable Long id) throws CustomerNotFoundException {
        CustomerDTO dto = customerService.findDTOById(id);
        if (dto == null) {
            log.debug("Customer with id={} not found", id);
            throw new CustomerNotFoundException(id);
        }
        return dto;
    }

    @GetMapping()
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomerDTOs();
    }

    @PostMapping("/register")
    public ResponseEntity<Object> addNewPerson(@RequestBody CustomerDTO customerDTO) {
        List<BodyReport> reports = customerValidator.validateCustomerAddDTO(customerDTO);
        if (reports.size() != 0) {
            return new ResponseEntity<>(reports, HttpStatus.BAD_REQUEST);
        }
        customerService.addNewCustomer(customerDTO);
        return ResponseEntity.created(URI.create("/customers")).build();
    }

    @GetMapping("/refreshToken")
    public ResponseEntity<Object> refreshAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authHeader.substring("Bearer ".length());
                JWTVerifier verifier = JWT.require(CustomFilter.algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                CustomerDTO customer = customerService.findDTOByEmail(username);
                String accessToken = JWT.create()
                        .withSubject(username)
                        .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                        .withClaim("roles", Stream.of("USER").collect(Collectors.toList()))
                        .withIssuer(request.getRequestURL().toString())
                        .sign(CustomFilter.algorithm);
                Map<String, String> tokenMap = new HashMap<>();
                tokenMap.put("access_token", accessToken);
                tokenMap.put("refresh_token", refreshToken);
                log.info("Token for {} has been updated", username);
                return new ResponseEntity<>(tokenMap, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(new BodyExceptionWrapper(HttpStatus.FORBIDDEN.value(), e.getMessage()), HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(new BodyExceptionWrapper(HttpStatus.FORBIDDEN.value(), "Authentication header is wrong or absent"), HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCustomer(@PathVariable Long id) {
        boolean f = customerService.removeById(id);
        if (!f){
            return ResponseEntity.noContent().build();
        }
        log.info("Customer id={} has been deleted", id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editCustomer(@RequestBody CustomerDTO customerDTO, @PathVariable("id") Long id) throws CustomerNotFoundException {
        List<BodyReport> reports = customerValidator.validateCustomerUpdateDTO(customerDTO, id);
        if (reports.size() != 0) return new ResponseEntity<>(reports, HttpStatus.CONFLICT);
        customerService.update(customerDTO, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/payMethods")
    public ResponseEntity<Collection<CustomerPayMethodDTO>> getAllPMByCustomerId(@PathVariable("id") Long id) throws CustomerNotFoundException {
        CustomerDTO dto = customerService.findDTOById(id);
        if (dto == null){
            log.debug("Customer with id={} not found", id);
            throw new CustomerNotFoundException(id);
        }
        return new ResponseEntity<>(dto.getPayMethods(), HttpStatus.OK);
    }

    @GetMapping("/{id}/helpWithOffers")
    public ResponseEntity<Object> getAllAccessibleOffers(@PathVariable("id") Long id) throws CustomerNotFoundException {
        if (!customerService.isExists(id)) {
            log.debug("Customer with id={} not found", id);
            throw new CustomerNotFoundException(id);
        }
        return ResponseEntity.ok(customerService.findOffers(id));
    }

    @GetMapping("/{id}/addresses")
    public ResponseEntity<Object> getAllADressesByCustomerId(@PathVariable("id") Long id){
        if (!customerService.isExists(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(customerService.findDTOById(id).getAddresses());
    }
}

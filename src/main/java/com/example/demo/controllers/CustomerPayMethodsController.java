package com.example.demo.controllers;

import com.example.demo.DTOs.CustomerPayMethodDTO;
import com.example.demo.exceptions.BodyReport;
import com.example.demo.services.CustomerPayMethodService;
import com.example.demo.validators.CurPayMethodValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/customerPayMethods")
@RequiredArgsConstructor
@Slf4j
public class CustomerPayMethodsController {
    private final CustomerPayMethodService customerPayMethodService;
    private final CurPayMethodValidator curPayMethodValidator;

    @GetMapping("/{id}")
    public CustomerPayMethodDTO getCurPayMethodById(@PathVariable("id") Long id){
        CustomerPayMethodDTO dto= customerPayMethodService.getDTOById(id);
        if (dto == null) {
            log.debug("CustomerPayMethod with id={} not found", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No pay method with id=" + id + " exists");
        }
        return dto;
    }

    @PostMapping
    public ResponseEntity<Object> addNewCurPayMethod(@RequestBody CustomerPayMethodDTO dto){
        List<BodyReport> reports=curPayMethodValidator.validateNewCurPayMethod(dto);
        if (reports.size()!=0) {
            return new ResponseEntity<>(reports, HttpStatus.BAD_REQUEST);
        }
        customerPayMethodService.addNewCurPayMethod(dto);
        return ResponseEntity.created(URI.create("/curPayMethods")).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCurPayMethod(@PathVariable("id") Long id){
        if (!customerPayMethodService.deleteCurPayMethod(id)) ResponseEntity.noContent().build();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object>  editCustomer(@RequestBody CustomerPayMethodDTO dto, @PathVariable("id") Long id) {
        List<BodyReport> reports=curPayMethodValidator.validateNewCurPayMethod(dto);
        if (reports.size()!=0) return new ResponseEntity<>(reports, HttpStatus.BAD_REQUEST);
        customerPayMethodService.updateCurPayMethod(dto, id);
        return ResponseEntity.ok().build();
    }
}

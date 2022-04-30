package com.example.demo.controllers;

import com.example.demo.DTOs.PayMethodDTO;
import com.example.demo.exceptions.BodyExceptionWrapper;
import com.example.demo.exceptions.BodyReport;
import com.example.demo.models.PayMethod;
import com.example.demo.services.CustomerPayMethodService;
import com.example.demo.services.PayMethodsService;
import com.example.demo.validators.PayMethodValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payMethods")
@Slf4j
public class PayMethodController {
    private final PayMethodsService payMethodsService;
    private final CustomerPayMethodService customerPayMethodService;
    private final PayMethodValidator payMethodValidator;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getPayMethodById(@PathVariable("id") Long id) {
        PayMethodDTO payMethodDTO = payMethodsService.getDTOById(id);
        if (payMethodDTO == null) {
            log.debug("PayMethod with id={} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(payMethodDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getAllPayMethods() {
        return new ResponseEntity<>(payMethodsService.getAllDTOs(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addNewPayMethod(@RequestBody PayMethodDTO payMethodDTO) {
        List<BodyReport> reports = payMethodValidator.validatePayMethod(payMethodDTO);
        if (reports.size() != 0) return new ResponseEntity<>(reports, HttpStatus.BAD_REQUEST);
        payMethodsService.addNewPayMethod(payMethodDTO);
        return ResponseEntity.created(URI.create("/payMethods")).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePayMethod(@PathVariable("id") Long id) {
        PayMethod payMethod = payMethodsService.getById(id);
        if (payMethod == null) return ResponseEntity.noContent().build();
        if (payMethodsService.isAttachedToOffers(id) || customerPayMethodService.countAllByPayMethod(payMethod) != 0) {
            log.debug("PayMethod with id={} has not been deleted (has attachments)", id);
            return new ResponseEntity<>(new BodyExceptionWrapper(HttpStatus.CONFLICT.value(), "Pay method with id=" + id + " is attached to customer or offer"), HttpStatus.CONFLICT);
        }
        payMethodsService.deletePayMethod(id);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePayMethod(@PathVariable("id") Long id, @RequestBody PayMethodDTO payMethodDTO) {
        List<BodyReport> reports = payMethodValidator.validatePayMethod(payMethodDTO);
        if (reports.size() != 0) return new ResponseEntity<>(reports, HttpStatus.CONFLICT);
        payMethodsService.updatePayMethod(payMethodDTO, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/isExists")
    public ResponseEntity<Void> isExistsByStatus(@PathVariable("id") Long id){
        if (payMethodsService.isExists(id)) return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }
}

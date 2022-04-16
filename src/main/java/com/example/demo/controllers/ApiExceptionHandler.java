package com.example.demo.controllers;

import com.example.demo.exceptions.BodyExceptionWrapper;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.exceptions.PayMethodNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = {CustomerNotFoundException.class})
    protected ResponseEntity<Object> handleCustomerNotFound(Exception ex) {
        return new ResponseEntity<>(new BodyExceptionWrapper(404, "Customer with id="+((CustomerNotFoundException)ex).getId()+" not Found"),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = {PayMethodNotFoundException.class})
    protected ResponseEntity<Object> handlePayMethodNotFound(Exception ex) {
        return new ResponseEntity<>(new BodyExceptionWrapper(404, "PayMethod with id="+((PayMethodNotFoundException)ex).getId()+" not Found"),HttpStatus.NOT_FOUND);
    }
}

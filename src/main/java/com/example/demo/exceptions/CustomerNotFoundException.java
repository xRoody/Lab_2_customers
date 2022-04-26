package com.example.demo.exceptions;

public class CustomerNotFoundException extends Exception{
    private Long id;

    public CustomerNotFoundException() {
    }

    public CustomerNotFoundException(Long id) {
        this.id = id;
    }

    public CustomerNotFoundException(String message, Long id) {
        super(message);
        this.id = id;
    }

    public CustomerNotFoundException(String message, Throwable cause, Long id) {
        super(message, cause);
        this.id = id;
    }

    public CustomerNotFoundException(Throwable cause, Long id) {
        super(cause);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

package com.example.demo.exceptions;

public class PayMethodNotFoundException extends RuntimeException{
    private Long id;

    public PayMethodNotFoundException(Long id) {
        this.id = id;
    }

    public PayMethodNotFoundException(String message, Long id) {
        super(message);
        this.id = id;
    }

    public PayMethodNotFoundException(String message, Throwable cause, Long id) {
        super(message, cause);
        this.id = id;
    }

    public PayMethodNotFoundException(Throwable cause, Long id) {
        super(cause);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

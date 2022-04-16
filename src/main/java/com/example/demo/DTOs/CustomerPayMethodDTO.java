package com.example.demo.DTOs;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class CustomerPayMethodDTO {
    private Long id;
    private String name;
    private Long customerId;
    private String data;
    private Long payId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerPayMethodDTO that = (CustomerPayMethodDTO) o;
        return Objects.equals(name, that.name) && Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, customerId);
    }
}

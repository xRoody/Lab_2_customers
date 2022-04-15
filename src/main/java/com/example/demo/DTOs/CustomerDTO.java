package com.example.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private Long id;
    private String login;
    private String password;
    private String email;
    private String lastName;
    private String firstName;
    private String dob; //date of birth
    private Collection<AddressDTO> addresses=new HashSet<>();
    private Collection<CurPayMethodDTO> payMethods=new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerDTO that = (CustomerDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(login, that.login) && Objects.equals(password, that.password) && Objects.equals(email, that.email) && Objects.equals(lastName, that.lastName) && Objects.equals(firstName, that.firstName) && Objects.equals(dob, that.dob) && Objects.equals(addresses, that.addresses) && Objects.equals(payMethods, that.payMethods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, email, lastName, firstName, dob, addresses, payMethods);
    }
}

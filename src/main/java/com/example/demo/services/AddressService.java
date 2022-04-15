package com.example.demo.services;

import com.example.demo.DTOs.AddressDTO;
import com.example.demo.models.Address;

public interface AddressService {
    Address getByDTO(AddressDTO dto);
    AddressDTO getDTObyObject(Address address);
}

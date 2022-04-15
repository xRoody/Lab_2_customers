package com.example.demo.services;

import com.example.demo.DTOs.AddressDTO;
import com.example.demo.models.Address;
import com.example.demo.repositories.AddressRepo;
import com.example.demo.repositories.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService{
    private final CustomerRepo customerRepo;
    private final AddressRepo addressRepo;

    public Address getByDTO(AddressDTO dto){
        return Address.builder().id(dto.getId()).title(dto.getTitle()).country(dto.getCountry()).town(dto.getTown()).street(dto.getStreet()).house(dto.getHouse()).customer(customerRepo.getById(dto.getCustomerId())).build();
    }

    public AddressDTO getDTObyObject(Address address){
        return AddressDTO.builder().id(address.getId()).title(address.getTitle()).country(address.getCountry()).house(address.getHouse()).street(address.getStreet()).town(address.getTown()).customerId(address.getCustomer().getId()).build();
    }
}

package com.example.demo.services;

import com.example.demo.DTOs.CustomerPayMethodDTO;
import com.example.demo.models.CustomerPayMethod;
import com.example.demo.models.PayMethod;
import com.example.demo.repositories.CustomerRepo;
import com.example.demo.repositories.CustomerPayMethodRepo;
import com.example.demo.repositories.PayMethodRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomerPayMethodServiceImpl implements CustomerPayMethodService {
    private final CustomerRepo customerRepo;
    private final CustomerPayMethodRepo customerPayMethodRepo;
    private final PayMethodRepo payMethodRepo;

    public CustomerPayMethodDTO getDTOById(Long id){
        return getDTObyObject(customerPayMethodRepo.findById(id).orElse(null));
    }

    public List<CustomerPayMethodDTO> getAllCurPayMethods(){
        return customerPayMethodRepo.findAll().stream().map(this::getDTObyObject).collect(Collectors.toList());
    }

    public void addNewCurPayMethod(CustomerPayMethodDTO customerPayMethodDTO){
        CustomerPayMethod customerPayMethod = CustomerPayMethod.builder()
                .name(customerPayMethodDTO.getName())
                .data(customerPayMethodDTO.getData())
                .customer(customerRepo.getById(customerPayMethodDTO.getCustomerId()))
                .payMethod(payMethodRepo.getById(customerPayMethodDTO.getPayId()))
                .build();
        customerPayMethodDTO.setId(customerPayMethodRepo.save(customerPayMethod).getId());
        log.info("New CustomerPayMethod {} has been added", customerPayMethodDTO);
    }

    public int countAllByPayMethod(PayMethod payMethod){
        int i= customerPayMethodRepo.countAllByPayMethod(payMethod);
        log.debug("{} CustomerPayMethods with payId={} ", i,payMethod.getId());
        return i;
    }

    public boolean deleteCurPayMethod(Long id){
        CustomerPayMethod customerPayMethod = customerPayMethodRepo.findById(id).orElse(null);
        boolean f=false;
        if (customerPayMethod !=null){
            customerPayMethodRepo.deleteById(id);
            f=true;
            log.info("Customer pay method {} has been deleted", customerPayMethod);
        }
        return f;
    }

    public void updateCurPayMethod(CustomerPayMethodDTO dto, Long id){
        CustomerPayMethod customerPayMethod = customerPayMethodRepo.getById(id);
        customerPayMethod.setName(dto.getName());
        customerPayMethod.setCustomer(customerRepo.getById(dto.getCustomerId()));
        customerPayMethod.setData(dto.getData());
        customerPayMethodRepo.save(customerPayMethod);
        log.info("CustomerPayMethod {} has been updated", dto);
    }

    public CustomerPayMethod getByDTO(CustomerPayMethodDTO dto){
        if (dto==null) return null;
        return CustomerPayMethod.builder().id(dto.getId()).name(dto.getName()).customer(customerRepo.getById(dto.getCustomerId())).data(dto.getData()).payMethod(payMethodRepo.getById(dto.getPayId())).build();
    }

    public CustomerPayMethodDTO getDTObyObject(CustomerPayMethod customerPayMethod){
        if (customerPayMethod ==null) return null;
        return CustomerPayMethodDTO.builder().id(customerPayMethod.getId()).name(customerPayMethod.getName()).customerId(customerPayMethod.getCustomer().getId()).data(customerPayMethod.getData()).payId(customerPayMethod.getPayMethod().getId()).build();
    }
}

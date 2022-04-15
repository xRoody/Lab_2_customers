package com.example.demo.services;

import com.example.demo.DTOs.CustomerDTO;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.models.CurPayMethod;
import com.example.demo.models.Customer;
import com.example.demo.repositories.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepo customerRepo;
    private final AddressService addressService;
    private final CurPayMethodService curPayMethodService;

    public Customer getById(Long id){
        return customerRepo.findById(id).orElse(null);
    }

    public boolean removeById(Long id){
        boolean f=false;
        Customer customer=getById(id);
        if (customer!=null){
            f=true;
            customer.getCurPayMethods().forEach(x->x.setCustomer(null));
            customerRepo.deleteById(id);
        }
        return f;
    }

    @Override
    public CustomerDTO findDTOById(Long id){
        Customer customer=customerRepo.findById(id).orElse(null);
        return getDTOByObject(customer);
    }

    private CustomerDTO getDTOByObject(Customer customer){
        if (customer==null) return null;
        CustomerDTO customerDTO=CustomerDTO.builder()
                .id(customer.getId())
                .dob(customer.getDob().toString())
                .email(customer.getEmail())
                .login(customer.getLogin())
                .password(customer.getPassword())
                .lastName(customer.getLastName())
                .firstName(customer.getFirstName())
                .addresses(customer.getAddresses().stream().map(x->addressService.getDTObyObject(x)).collect(Collectors.toSet()))
                .payMethods(customer.getCurPayMethods().stream().map(x-> curPayMethodService.getDTObyObject(x)).collect(Collectors.toSet()))
                .build();
        return customerDTO;
    }

    @Override
    /*
    * @TODO: add password encoding here
    * */
    public void addNewCustomer(CustomerDTO dto){
        Customer customer=Customer.builder()
                .dob(LocalDate.parse(dto.getDob()))
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .login(dto.getLogin())
                .password(dto.getPassword()) //encode here !!!
                .build();
        customer=customerRepo.save(customer);
        customer.setCurPayMethods(new HashSet<>());
        customer.setAddresses(new HashSet<>());
        Long id=customer.getId();
        customer.getAddresses().addAll(dto.getAddresses().stream().map(x->{
            x.setCustomerId(id);
            return addressService.getByDTO(x);
        }).collect(Collectors.toSet()));
        customer.getCurPayMethods().addAll(dto.getPayMethods().stream().map(x->{
            x.setCustomerId(id);
            CurPayMethod curPayMethod=curPayMethodService.getByDTO(x);
            return curPayMethod;
        }).collect(Collectors.toSet()));
    }

    public List<CustomerDTO> getAllCustomerDTOs(){
        List<CustomerDTO> customerDTOS=new ArrayList<>();
        for (Customer customer:customerRepo.findAll()){
            customerDTOS.add(getDTOByObject(customer));
        }
        return customerDTOS;
    }

    public void update(CustomerDTO customerDTO, Long id) throws CustomerNotFoundException {
        Customer customer=customerRepo.findById(id).orElse(null);
        if (customer==null) throw new CustomerNotFoundException(id);
        customer.setLogin(customerDTO.getLogin());
        customer.setPassword(customerDTO.getPassword());
        customer.setEmail(customerDTO.getEmail());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setDob(LocalDate.parse(customerDTO.getDob()));
        customer.setAddresses(customerDTO.getAddresses().stream().map(x->addressService.getByDTO(x)).collect(Collectors.toSet()));
        customer.setCurPayMethods(customerDTO.getPayMethods().stream().map(x-> curPayMethodService.getByDTO(x)).collect(Collectors.toSet()));
        customerRepo.save(customer);
    }

    @Override
    public Boolean isEmailExists(String email) {
        return customerRepo.findByEmail(email).isPresent();
    }

    @Override
    public Boolean isLoginExists(String login) {
        return customerRepo.findByLogin(login).isPresent();
    }

    @Override
    public Boolean isExists(Long id) {
        return customerRepo.findById(id).isPresent();
    }
}

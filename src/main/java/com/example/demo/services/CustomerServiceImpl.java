package com.example.demo.services;

import com.example.demo.DTOs.CustomerDTO;
import com.example.demo.DTOs.LoginFormDTO;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.models.CustomerPayMethod;
import com.example.demo.models.Customer;
import com.example.demo.models.PayMethod;
import com.example.demo.repositories.CustomerRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepo customerRepo;
    private final AddressService addressService;
    private final CustomerPayMethodService customerPayMethodService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final WebClient client;

    public Customer getById(Long id){
        return customerRepo.findById(id).orElse(null);
    }

    public boolean removeById(Long id){
        boolean f=false;
        Customer customer=getById(id);
        if (customer!=null){
            f=true;
            log.info("Customer {} has been deleted", getDTOByObject(customer));
            customer.getCustomerPayMethods().forEach(x->x.setCustomer(null));
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
        return CustomerDTO.builder()
                .id(customer.getId())
                .dob(customer.getDob().toString())
                .email(customer.getEmail())
                .login(customer.getLogin())
                .password(customer.getPassword())
                .lastName(customer.getLastName())
                .firstName(customer.getFirstName())
                .addresses(customer.getAddresses().stream().map(x->addressService.getDTObyObject(x)).collect(Collectors.toSet()))
                .payMethods(customer.getCustomerPayMethods().stream().map(x-> customerPayMethodService.getDTObyObject(x)).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public void addNewCustomer(CustomerDTO dto){
        Customer customer=Customer.builder()
                .dob(LocalDate.parse(dto.getDob()))
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .login(dto.getLogin())
                .password(passwordEncoder.encode(dto.getPassword())) //encode here !!!
                .build();
        customer=customerRepo.save(customer);
        customer.setCustomerPayMethods(new HashSet<>());
        customer.setAddresses(new HashSet<>());
        Long id=customer.getId();
        customer.getAddresses().addAll(dto.getAddresses().stream().map(x->{
            x.setCustomerId(id);
            return addressService.getByDTO(x);
        }).collect(Collectors.toSet()));
        customer.getCustomerPayMethods().addAll(dto.getPayMethods().stream().map(x->{
            x.setCustomerId(id);
            CustomerPayMethod customerPayMethod = customerPayMethodService.getByDTO(x);
            return customerPayMethod;
        }).collect(Collectors.toSet()));
        log.info("Customer id={} has been added", customer.getId());
    }

    public List<CustomerDTO> getAllCustomerDTOs(){
        List<CustomerDTO> customerDTOS=new ArrayList<>();
        for (Customer customer:customerRepo.findAll()){
            customerDTOS.add(getDTOByObject(customer));
        }
        return customerDTOS;
    }

    @Override
    public Object findOffers(Long id) {
        List<Long> longs=getById(id).getCustomerPayMethods().stream().map(x->x.getId()).collect(Collectors.toList());
        Object ob=client.post().uri("/offers/help", id).bodyValue(longs).retrieve().bodyToMono(Object.class).block(); //to offer dto list
        log.info("{} by id={}", ob, id);
        return ob;
    }

    public void update(CustomerDTO customerDTO, Long id) throws CustomerNotFoundException {
        Customer customer=customerRepo.findById(id).orElse(null);
        if (customer==null) throw new CustomerNotFoundException(id);
        customer.setLogin(customerDTO.getLogin());
        customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        customer.setEmail(customerDTO.getEmail());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setDob(LocalDate.parse(customerDTO.getDob()));
        customer.setAddresses(customerDTO.getAddresses().stream().map(x->addressService.getByDTO(x)).collect(Collectors.toSet()));
        customer.setCustomerPayMethods(customerDTO.getPayMethods().stream().map(x-> customerPayMethodService.getByDTO(x)).collect(Collectors.toSet()));
        customerRepo.save(customer);
        log.info("Customer {} has been updated", customerDTO);
    }

    @Override
    public Boolean isEmailExists(String email) {
        return customerRepo.findOneByEmail(email).isPresent();
    }

    @Override
    public Boolean isLoginExists(String login) {
        return customerRepo.findOneByLogin(login).isPresent();
    }

    @Override
    public Boolean isExists(Long id) {
        return customerRepo.findById(id).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer=customerRepo.findOneByEmail(email).orElseThrow(()->new UsernameNotFoundException("This email is not exists"));
        Collection<GrantedAuthority> grantedAuthorities= Stream.of(new SimpleGrantedAuthority("USER")).collect(Collectors.toList());
        return new User(email, customer.getPassword(), grantedAuthorities);
    }
    @Override
    public CustomerDTO findDTOByEmail(String email){
        Customer customer=customerRepo.findOneByEmail(email).orElseThrow(()->new UsernameNotFoundException("This email is not exists"));
        return getDTOByObject(customer);
    }
}

package com.example.demo.services;

import com.example.demo.DTOs.PayMethodDTO;
import com.example.demo.exceptions.PayMethodNotFoundException;
import com.example.demo.models.PayMethod;
import com.example.demo.repositories.PayMethodRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayMethodsServiceImpl implements PayMethodsService{
    private final PayMethodRepo payMethodRepo;
    private final WebClient client;
    @Override
    public void addNewPayMethod(PayMethodDTO payMethodDTO) {
        PayMethod payMethod=getByDTO(payMethodDTO);
        payMethodRepo.save(payMethod);
        log.info("PayMethod {} has been added", payMethodDTO);
    }

    private PayMethod getByDTO(PayMethodDTO payMethodDTO){
        return PayMethod.builder().id(payMethodDTO.getId()).title(payMethodDTO.getTitle()).data(payMethodDTO.getData()).build();
    }

    private PayMethodDTO getDTOByObj(PayMethod payMethod){
        return PayMethodDTO.builder()
                .id(payMethod.getId())
                .data(payMethod.getData())
                .title(payMethod.getTitle())
                .build();
    }

    public boolean isAttachedToOffers(Long id){
        Integer i=client.get().uri("/offers/countWithPayMethod/{id}", id).retrieve().bodyToMono(Integer.class).block();
        log.info(" {} offers by payId={}", i, id);
        return !Objects.equals(i, 0);
    }

    public PayMethod getById(Long id){
        return payMethodRepo.findById(id).orElse(null);
    }

    @Override
    public boolean deletePayMethod(Long id) {
        PayMethod payMethod=payMethodRepo.findById(id).orElse(null);
        if (payMethod==null) return false;
        payMethodRepo.deleteById(id);
        log.info("PayMethod {} has been deleted", payMethod);
        return true;
    }

    @Override
    public PayMethodDTO getDTOById(Long id) {
        PayMethod payMethod=payMethodRepo.findById(id).orElse(null);
        if (payMethod==null) return null;
        return getDTOByObj(payMethod);
    }

    @Override
    public List<PayMethodDTO> getAllDTOs() {
        return payMethodRepo.findAll().stream().map(x->getDTOByObj(x)).collect(Collectors.toList());
    }

    @Override
    public void updatePayMethod(PayMethodDTO payMethodDTO, Long id) {
        PayMethod payMethod=payMethodRepo.findById(id).orElse(null);
        if (payMethod==null) throw new PayMethodNotFoundException(id);
        payMethod.setTitle(payMethodDTO.getTitle());
        payMethod.setData(payMethodDTO.getData());
        payMethodRepo.save(payMethod);
        log.info("PayMethod {} has been updated", payMethodDTO);
    }

    @Override
    public Boolean isExists(Long id) {
        return payMethodRepo.findById(id).isPresent();
    }
}

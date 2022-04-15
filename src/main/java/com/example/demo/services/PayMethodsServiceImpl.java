package com.example.demo.services;

import com.example.demo.DTOs.PayMethodDTO;
import com.example.demo.exceptions.PayMethodNotFoundException;
import com.example.demo.models.PayMethod;
import com.example.demo.repositories.PayMethodRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayMethodsServiceImpl implements PayMethodsService{
    private final PayMethodRepo payMethodRepo;
    @Override
    public void addNewPayMethod(PayMethodDTO payMethodDTO) {
        PayMethod payMethod=getByDTO(payMethodDTO);
        payMethodRepo.save(payMethod);
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

    @Override
    public boolean deletePayMethod(Long id) {
        PayMethod payMethod=payMethodRepo.findById(id).orElse(null);
        if (payMethod==null) return false;
        //check
        payMethodRepo.deleteById(id);
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
    }

    @Override
    public Boolean isExists(Long id) {
        return payMethodRepo.findById(id).isPresent();
    }
}

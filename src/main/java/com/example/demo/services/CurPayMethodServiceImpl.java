package com.example.demo.services;

import com.example.demo.DTOs.CurPayMethodDTO;
import com.example.demo.models.CurPayMethod;
import com.example.demo.repositories.CustomerRepo;
import com.example.demo.repositories.CurPayMethodRepo;
import com.example.demo.repositories.PayMethodRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CurPayMethodServiceImpl implements CurPayMethodService {
    private final CustomerRepo customerRepo;
    private final CurPayMethodRepo curPayMethodRepo;
    private final PayMethodRepo payMethodRepo;

    public CurPayMethodDTO getDTOById(Long id){
        return getDTObyObject(curPayMethodRepo.findById(id).orElse(null));
    }

    public List<CurPayMethodDTO> getAllCurPayMethods(){
        return curPayMethodRepo.findAll().stream().map(x->getDTObyObject(x)).collect(Collectors.toList());
    }

    public void addNewCurPayMethod(CurPayMethodDTO curPayMethodDTO){
        CurPayMethod curPayMethod = CurPayMethod.builder()
                .name(curPayMethodDTO.getName())
                .data(curPayMethodDTO.getData())
                .customer(customerRepo.getById(curPayMethodDTO.getCustomerId()))
                .payMethod(payMethodRepo.getById(curPayMethodDTO.getPayId()))
                .build();
        curPayMethodRepo.save(curPayMethod);
    }

    public boolean deleteCurPayMethod(Long id){
        CurPayMethod curPayMethod = curPayMethodRepo.findById(id).orElse(null);
        boolean f=false;

        if (curPayMethod !=null){
            curPayMethodRepo.deleteById(id);
            f=true;
        }
        return f;
    }

    public void updateCurPayMethod(CurPayMethodDTO dto, Long id){
        CurPayMethod curPayMethod = curPayMethodRepo.getById(id);
        curPayMethod.setName(dto.getName());
        curPayMethod.setCustomer(customerRepo.getById(dto.getCustomerId()));
        curPayMethod.setData(dto.getData());
        curPayMethodRepo.save(curPayMethod);
    }

    public CurPayMethod getByDTO(CurPayMethodDTO dto){
        if (dto==null) return null;
        return CurPayMethod.builder().id(dto.getId()).name(dto.getName()).customer(customerRepo.getById(dto.getCustomerId())).data(dto.getData()).payMethod(payMethodRepo.getById(dto.getPayId())).build();
    }
    public CurPayMethodDTO getDTObyObject(CurPayMethod curPayMethod){
        if (curPayMethod ==null) return null;
        return CurPayMethodDTO.builder().id(curPayMethod.getId()).name(curPayMethod.getName()).customerId(curPayMethod.getCustomer().getId()).data(curPayMethod.getData()).payId(curPayMethod.getPayMethod().getId()).build();
    }
}

package com.example.demo.controllers;

import com.example.demo.DTOs.PayMethodDTO;
import com.example.demo.services.PayMethodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payMethods")
public class PayMethodController {
    private final PayMethodsService payMethodsService;

    @GetMapping("/{id}")
    public PayMethodDTO getPayMethodById(@PathVariable("id") Long id){
        return payMethodsService.getDTOById(id);
    }

    @GetMapping
    public List<PayMethodDTO> getAllPayMethods(){
        return payMethodsService.getAllDTOs();
    }

    @PostMapping
    public void addNewPayMethod(@RequestBody PayMethodDTO payMethodDTO){

        payMethodsService.addNewPayMethod(payMethodDTO);
    }

    @DeleteMapping("/{id}")
    public void deletePayMethod(@PathVariable("id") Long id){
        //if((/*attached to offer? || payMethod.getCustomer()!=null*/)) throw new PayMethodHasAttachments();
        payMethodsService.deletePayMethod(id);
    }

    @PutMapping("/{id}")
    public void updatePayMethod(@PathVariable("id") Long id, @RequestBody PayMethodDTO payMethodDTO){
        payMethodsService.updatePayMethod(payMethodDTO,id);
    }
}

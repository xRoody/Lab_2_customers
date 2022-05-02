package com.example.demo.DTOs;

import java.util.ArrayList;
import java.util.List;

public class OfferDTO {
    private Long id;
    private String title;
    private Double price;
    private List<CharacteristicDTO> characteristics=new ArrayList<>();
    private Long categoryId;
    private Long payMethod;
}

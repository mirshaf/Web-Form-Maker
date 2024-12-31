package com.example.web_spring_thingy.dto;

import lombok.Data;

import java.util.List;

@Data
public class FormDTO {
    private Long id;
    private String name;
    private boolean releaseStatus;
    private List<FieldDTO> fields;
}
package com.example.web_spring_thingy.service;

import com.example.web_spring_thingy.dto.FieldDTO;
import com.example.web_spring_thingy.dto.FormDTO;

import java.util.List;

public interface FormService {
    List<FormDTO> getAllForms();
    FormDTO createForm(FormDTO formDTO);
    FormDTO getFormById(Long id);
    void deleteFormById(Long id);
    FormDTO updateForm(Long id, FormDTO formDTO);
    List<FieldDTO> getFieldsOfForm(Long id);
    void publishForm(Long id);
    void updateFormFields(Long id, List<FieldDTO> fields);
    List<FormDTO> getPublishedForms();
}
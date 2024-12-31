package com.example.web_spring_thingy.service.impl;

import com.example.web_spring_thingy.dto.FieldDTO;
import com.example.web_spring_thingy.dto.FormDTO;
import com.example.web_spring_thingy.exception.ResourceNotFoundException;
import com.example.web_spring_thingy.model.Field;
import com.example.web_spring_thingy.model.Form;
import com.example.web_spring_thingy.repository.FormRepository;
import com.example.web_spring_thingy.service.FormService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormServiceImpl implements FormService {

    private final FormRepository formRepository;

    public FormServiceImpl(FormRepository formRepository) {
        this.formRepository = formRepository;
    }

    @PostConstruct
    public void loadSampleData() {
        Form form1 = new Form();
        form1.setName("Contact Form");
        form1.setReleaseStatus(true);

        Field field1 = new Field();
        field1.setName("Name");
        field1.setLabel("Full Name");
        field1.setType("Text");
        field1.setDefaultValue("");
        field1.setForm(form1);

        Field field2 = new Field();
        field2.setName("Email");
        field2.setLabel("Email Address");
        field2.setType("Text");
        field2.setDefaultValue("");
        field2.setForm(form1);

        form1.getFields().add(field1);
        form1.getFields().add(field2);

        // Create more forms
        Form form2 = new Form();
        form2.setName("Feedback Form");
        form2.setReleaseStatus(false);

        Field field3 = new Field();
        field3.setName("Feedback");
        field3.setLabel("Your Feedback");
        field3.setType("Text");
        field3.setDefaultValue("");
        field3.setForm(form2);

        form2.getFields().add(field3);

        Form form3 = new Form();
        form3.setName("Survey Form");
        form3.setReleaseStatus(true);

        Field field4 = new Field();
        field4.setName("Age");
        field4.setLabel("Your Age");
        field4.setType("Number");
        field4.setDefaultValue("");
        field4.setForm(form3);

        Field field5 = new Field();
        field5.setName("Agree");
        field5.setLabel("Do you agree?");
        field5.setType("Boolean");
        field5.setDefaultValue("false");
        field5.setForm(form3);

        form3.getFields().add(field4);
        form3.getFields().add(field5);

        formRepository.saveAll(List.of(form1, form2, form3));
    }

    @Override
    public List<FormDTO> getAllForms() {
        return formRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FormDTO createForm(FormDTO formDTO) {
        Form form = new Form();
        form.setName(formDTO.getName());
        form.setReleaseStatus(false); // Newly created forms are not released by default

        // Map fields from DTO to entity
        for (FieldDTO fieldDTO : formDTO.getFields()) {
            Field field = new Field();
            field.setName(fieldDTO.getName());
            field.setLabel(fieldDTO.getLabel());
            field.setType(fieldDTO.getType());
            field.setDefaultValue(fieldDTO.getDefaultValue());
            field.setForm(form); // Establish the relationship
            form.getFields().add(field);
        }

        // Save and return the saved form as a DTO
        Form savedForm = formRepository.save(form);
        return mapToDTO(savedForm);
    }

    @Override
    public FormDTO getFormById(Long id) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Form with ID " + id + " not found"));
        return mapToDTO(form);
    }

    @Override
    public void deleteFormById(Long id) {
        if (!formRepository.existsById(id)) {
            throw new ResourceNotFoundException("Form with ID " + id + " not found");
        }
        formRepository.deleteById(id);
    }

    @Override
    public FormDTO updateForm(Long id, FormDTO formDTO) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Form with ID " + id + " not found"));

        form.setName(formDTO.getName());

        // Update fields
        form.getFields().clear();
        for (FieldDTO fieldDTO : formDTO.getFields()) {
            Field field = new Field();
            field.setName(fieldDTO.getName());
            field.setLabel(fieldDTO.getLabel());
            field.setType(fieldDTO.getType());
            field.setDefaultValue(fieldDTO.getDefaultValue());
            field.setForm(form);
            form.getFields().add(field);
        }

        Form savedForm = formRepository.save(form);
        return mapToDTO(savedForm);
    }

    @Override
    public List<FieldDTO> getFieldsOfForm(Long id) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Form with ID " + id + " not found"));

        return form.getFields().stream().map(field -> {
            FieldDTO fieldDTO = new FieldDTO();
            fieldDTO.setId(field.getId());
            fieldDTO.setName(field.getName());
            fieldDTO.setLabel(field.getLabel());
            fieldDTO.setType(field.getType());
            fieldDTO.setDefaultValue(field.getDefaultValue());
            return fieldDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void publishForm(Long id) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Form with ID " + id + " not found"));

//        form.setReleaseStatus(true);
        form.flipReleaseStatus();
        formRepository.save(form);
    }

    @Override
    public void updateFormFields(Long id, List<FieldDTO> fieldsDTO) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Form with ID " + id + " not found"));

        form.getFields().clear();
        for (FieldDTO fieldDTO : fieldsDTO) {
            Field field = new Field();
            field.setName(fieldDTO.getName());
            field.setLabel(fieldDTO.getLabel());
            field.setType(fieldDTO.getType());
            field.setDefaultValue(fieldDTO.getDefaultValue());
            field.setForm(form);
            form.getFields().add(field);
        }

        formRepository.save(form);
    }

    @Override
    public List<FormDTO> getPublishedForms() {
        List<Form> publishedForms = formRepository.findByReleaseStatus(true);
        return publishedForms.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private FormDTO mapToDTO(Form form) {
        FormDTO dto = new FormDTO();
        dto.setId(form.getId());
        dto.setName(form.getName());
        dto.setReleaseStatus(form.isReleaseStatus());

        // Map fields
        dto.setFields(form.getFields().stream().map(field -> {
            FieldDTO fieldDTO = new FieldDTO();
            fieldDTO.setId(field.getId());
            fieldDTO.setName(field.getName());
            fieldDTO.setLabel(field.getLabel());
            fieldDTO.setType(field.getType());
            fieldDTO.setDefaultValue(field.getDefaultValue());
            return fieldDTO;
        }).collect(Collectors.toList()));

        return dto;
    }
}
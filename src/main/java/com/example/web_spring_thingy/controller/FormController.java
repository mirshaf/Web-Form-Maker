package com.example.web_spring_thingy.controller;

import com.example.web_spring_thingy.dto.FieldDTO;
import com.example.web_spring_thingy.dto.FormDTO;
import com.example.web_spring_thingy.service.FormService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forms")
public class FormController {

    private final FormService formService;

    public FormController(FormService formService) {
        this.formService = formService;
    }

    // GET: Get all forms
    @GetMapping("/")
    public List<FormDTO> getAllForms() {
        return formService.getAllForms();
    }

    // POST: Create a new form
    @PostMapping("/")
    public ResponseEntity<?> createForm(@RequestBody FormDTO formDTO) {
        // Manual validation
        if (formDTO.getName() == null || formDTO.getName().isBlank()) {
            return ResponseEntity.badRequest().body("Form name cannot be null or blank");
        }

        if (formDTO.getFields() == null || formDTO.getFields().isEmpty()) {
            return ResponseEntity.badRequest().body("Form must have at least one field");
        }

        for (FieldDTO field : formDTO.getFields()) {
            if (field.getName() == null || field.getName().isBlank()) {
                return ResponseEntity.badRequest().body("Field name cannot be null or blank");
            }
            if (field.getLabel() == null || field.getLabel().isBlank()) {
                return ResponseEntity.badRequest().body("Field label cannot be null or blank");
            }
            if (field.getType() == null || field.getType().isBlank()) {
                return ResponseEntity.badRequest().body("Field type cannot be null or blank");
            }
        }

        // If validation passes, proceed to create the form
        FormDTO createdForm = formService.createForm(formDTO);
        return new ResponseEntity<>(createdForm, HttpStatus.CREATED);
    }

    // GET: Get a specific form by ID
    @GetMapping("/{id}")
    public ResponseEntity<FormDTO> getFormById(@PathVariable Long id) {
        FormDTO formDTO = formService.getFormById(id);
        return new ResponseEntity<>(formDTO, HttpStatus.OK);
    }

    // DELETE: Delete a specific form by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormById(@PathVariable Long id) {
        formService.deleteFormById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FormDTO> updateForm(@PathVariable Long id, @RequestBody FormDTO formDTO) {
        FormDTO updatedForm = formService.updateForm(id, formDTO);
        return new ResponseEntity<>(updatedForm, HttpStatus.OK);
    }

    @GetMapping("/{id}/fields")
    public ResponseEntity<List<FieldDTO>> getFieldsOfForm(@PathVariable Long id) {
        List<FieldDTO> fields = formService.getFieldsOfForm(id);
        return new ResponseEntity<>(fields, HttpStatus.OK);
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<Void> publishForm(@PathVariable Long id) {
        formService.publishForm(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/fields")
    public ResponseEntity<?> updateFormFields(@PathVariable Long id, @RequestBody List<FieldDTO> fields) {
        // Manual validation
        if (fields == null || fields.isEmpty()) {
            return ResponseEntity.badRequest().body("Fields cannot be null or empty");
        }

        for (FieldDTO field : fields) {
            if (field.getName() == null || field.getName().isBlank()) {
                return ResponseEntity.badRequest().body("Field name cannot be null or blank");
            }
            if (field.getLabel() == null || field.getLabel().isBlank()) {
                return ResponseEntity.badRequest().body("Field label cannot be null or blank");
            }
            if (field.getType() == null || field.getType().isBlank()) {
                return ResponseEntity.badRequest().body("Field type cannot be null or blank");
            }
        }

        // If validation passes, proceed to update the fields
        formService.updateFormFields(id, fields);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/published")
    public ResponseEntity<List<FormDTO>> getPublishedForms() {
        List<FormDTO> publishedForms = formService.getPublishedForms();
        return new ResponseEntity<>(publishedForms, HttpStatus.OK);
    }
}
package com.example.web_spring_thingy.repository;

import com.example.web_spring_thingy.model.Form;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormRepository extends JpaRepository<Form, Long> {
    List<Form> findByReleaseStatus(boolean releaseStatus);
}
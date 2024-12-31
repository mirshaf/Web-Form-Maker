package com.example.web_spring_thingy.repository;

import com.example.web_spring_thingy.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldRepository extends JpaRepository<Field, Long> {
}
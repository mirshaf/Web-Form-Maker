package com.example.web_spring_thingy.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Form {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private boolean releaseStatus;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Field> fields = new HashSet<>();

    @Override
    public String toString() {
        return "Form{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", releaseStatus=" + releaseStatus +
                '}';
    }

    public void flipReleaseStatus() {
        this.releaseStatus = !this.releaseStatus;
    }
}
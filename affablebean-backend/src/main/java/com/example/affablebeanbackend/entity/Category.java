package com.example.affablebeanbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @JsonIgnore //must have at one side if relationship is bidirectional(protect StackOverFlow exception)
    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();
}

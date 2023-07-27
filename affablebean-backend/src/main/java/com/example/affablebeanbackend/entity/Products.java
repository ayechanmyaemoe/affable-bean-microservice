package com.example.affablebeanbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Data
@AllArgsConstructor
public class Products {

    private List<Product> products;

    public Products() {

    }

    public Products(Spliterator<Product> spliterator) {
        products = StreamSupport.stream(spliterator, false)
                .collect(Collectors.toList());
    }
}

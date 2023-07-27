package com.example.affablebeanbackend.controller;

import com.example.affablebeanbackend.dao.ProductDao;
import com.example.affablebeanbackend.entity.Product;
import com.example.affablebeanbackend.entity.Products;
import com.example.affablebeanbackend.service.ProductService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/backend")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public Products listProducts() {
        return productService.listAllProducts();
    }

}

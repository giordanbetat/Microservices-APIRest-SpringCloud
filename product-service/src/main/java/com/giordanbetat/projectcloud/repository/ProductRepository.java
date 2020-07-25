package com.giordanbetat.projectcloud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.giordanbetat.projectcloud.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}

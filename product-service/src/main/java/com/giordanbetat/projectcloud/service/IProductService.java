package com.giordanbetat.projectcloud.service;

import java.util.List;

import com.giordanbetat.projectcloud.model.Product;

public interface IProductService {

	public List<Product> findAll();
	
	public Product findById(Long id);
	
	public Product save(Product product);
	
	public void deleteById(Long id);
	
}

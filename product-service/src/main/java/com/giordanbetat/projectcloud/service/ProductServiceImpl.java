package com.giordanbetat.projectcloud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.giordanbetat.projectcloud.model.Product;
import com.giordanbetat.projectcloud.repository.ProductRepository;

@Service
public class ProductServiceImpl implements IProductService {

	@Autowired
	private ProductRepository repository;

	@Override
	@Transactional(readOnly = true)
	public List<Product> findAll() {
		return repository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Product findById(Long id) {
		return repository.findById(id).get();
	}

	@Override
	@Transactional
	public Product save(Product product) {

		return repository.save(product);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {

		repository.deleteById(id);
	}

}

package com.giordanbetat.projectcloud.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.giordanbetat.projectcloud.model.Product;
import com.giordanbetat.projectcloud.service.IProductService;

@RestController
public class ProductController {

	@Value(value = "${server.port}")
	private Integer port;

	@Autowired
	private IProductService service;

	@GetMapping(value = "/findAll")
	public List<Product> findAll() {
		return service.findAll().stream().map(product -> {
			product.setPort(port);
			return product;
		}).collect(Collectors.toList());
	}

	@GetMapping(value = "/findById/{id}")
	public Product findById(@PathVariable(value = "id") Long id) {

		Product product = service.findById(id);
		product.setPort(port);
		
		return product;
	}
	
	@PostMapping(value = "/save")
	@ResponseStatus(HttpStatus.CREATED)
	public Product save(@RequestBody Product product) {
		
		return service.save(product);
	}
	
	@PutMapping("/edit/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Product edit(@RequestBody Product product, @PathVariable Long id) {
		
		Product productAux = service.findById(id);
		productAux.setName(product.getName());
		productAux.setPrice(product.getPrice());
		
		return service.save(productAux);
	}
	
	@DeleteMapping("/delete/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable(value = "id") Long id) {
		
		service.deleteById(id);
	}
}

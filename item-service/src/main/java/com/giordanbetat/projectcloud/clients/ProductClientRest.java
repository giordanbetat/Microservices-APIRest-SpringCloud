package com.giordanbetat.projectcloud.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.giordanbetat.projectcloud.model.Product;

@FeignClient(name = "product-service")
public interface ProductClientRest {

	@GetMapping(value = "/findAll")
	public List<Product> findAll();
	
	@GetMapping(value = "/findById/{id}")
	public Product findById(@PathVariable(value = "id") Long id);
	
	@PostMapping(value = "/save")
	public Product save(@RequestBody Product product);
	
	@PutMapping(value = "/edit/{id}")
	public Product update(@RequestBody Product product, @PathVariable(value = "id") Long id);
	
	@DeleteMapping(value = "/delete/{id}")
	public void delete(@PathVariable(value = "id") Long id);
}
 
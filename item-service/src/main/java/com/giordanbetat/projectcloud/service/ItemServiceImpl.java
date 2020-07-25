package com.giordanbetat.projectcloud.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.giordanbetat.projectcloud.model.Item;
import com.giordanbetat.projectcloud.model.Product;

@Service(value = "serviceRestTemplate")
public class ItemServiceImpl implements ItemService {

	@Autowired
	private RestTemplate clientRest;

	@Override
	public List<Item> findAll() {

		List<Product> products = Arrays
				.asList(clientRest.getForObject("http://product-service/findAll", Product[].class));

		return products.stream().map(product -> new Item(product, 1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer amount) {

		Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", id.toString());

		Product product = clientRest.getForObject("http://product-service/findById/{id}", Product.class, pathVariables);

		return new Item(product, amount);
	}

	@Override
	public Product save(Product product) {

		HttpEntity<Product> request = new HttpEntity<Product>(product);

		ResponseEntity<Product> response = clientRest.exchange("http://product-service/save", HttpMethod.POST, request,
				Product.class);
		
		return response.getBody();
	}

	@Override
	public Product update(Product product, Long id) {

		Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", id.toString());

		HttpEntity<Product> request = new HttpEntity<Product>(product);
		ResponseEntity<Product> response = clientRest.exchange("http://product-service/edit/{id}", HttpMethod.PUT,
				request, Product.class, pathVariables);

		return response.getBody();
	}

	@Override
	public void delete(Long id) {
		Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", id.toString());
		
		clientRest.delete("http://product-service/delete/{id}",pathVariables);
	}

}

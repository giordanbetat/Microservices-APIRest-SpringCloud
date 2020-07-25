package com.giordanbetat.projectcloud.service;

import java.util.List;

import com.giordanbetat.projectcloud.model.Item;
import com.giordanbetat.projectcloud.model.Product;

public interface ItemService {

	public List<Item> findAll();
	
	public Item findById(Long id, Integer amount);
	
	public Product save(Product product);
	
	public Product update(Product product, Long id);
	
	public void delete(Long id);
	
}

package com.giordanbetat.projectcloud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.giordanbetat.projectcloud.model.Item;
import com.giordanbetat.projectcloud.model.Product;
import com.giordanbetat.projectcloud.service.ItemService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RefreshScope
@RestController
public class ItemController {
	
	private static Logger log = LoggerFactory.getLogger(ItemController.class);
	
	@Autowired
	private Environment environment;

	@Autowired
	@Qualifier(value = "serviceFeign")
	private ItemService service;
	
	@Value("${configuration.text}")
	private String text;

	@GetMapping(value = "/findAll") 
	public List<Item> findAll() {
		
		return service.findAll();
	}
	
	@HystrixCommand(fallbackMethod = "alternative")
	@GetMapping(value = "/findById/{id}/amount/{amount}")
	public Item findById(@PathVariable(value = "id") Long id, @PathVariable(value = "amount") Integer amount) {
		
		return service.findById(id, amount); 
	}
	
	public Item alternative(Long id, Integer amount) {
		
		Item item = new Item();
		
		Product product = new Product();
		
		item.setAmount(amount);
		product.setId(id);
		product.setName("Camera Sony");
		product.setPrice(500.0);
		item.setProduct(product);
		
		return item;
	}
	
	@GetMapping("/getConfig")
	public ResponseEntity<?> getConfig(@Value("${server.port}") String port){
		
		log.info(text);
		Map<String, String> json = new HashMap<>();
		json.put("text", text);
		json.put("port", port);
		
		if(environment.getActiveProfiles().length > 0 && environment.getActiveProfiles()[0].equals("dev")) {
			json.put("autor.name", environment.getProperty("configuration.autor.name"));
			json.put("autor.email", environment.getProperty("configuration.autor.email"));
		}
		
		return new ResponseEntity<Map<String, String>>(json, HttpStatus.OK); 
	}
	
	@PostMapping("/save")
	@ResponseStatus(HttpStatus.CREATED)
	public Product save(@RequestBody Product product) {
		
		return service.save(product);
	}
	
	@PutMapping("/edit/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Product edit(@RequestBody Product product, @PathVariable(value = "id") Long id) {
		
		return service.update(product, id);
	}
	
	@DeleteMapping("/delete/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable(value = "id") Long id) {
		
		service.delete(id);
	}

}

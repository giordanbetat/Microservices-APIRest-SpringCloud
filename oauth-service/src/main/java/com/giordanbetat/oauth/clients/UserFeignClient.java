package com.giordanbetat.oauth.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.giordanbetat.users.commons.models.Person;

@FeignClient(name = "users-service")
public interface UserFeignClient {

	@GetMapping("/persons/search/search-username")
	public Person findByUsername(@RequestParam(value = "username") String username);
	
	@PutMapping("/persons/{id}")
	public Person update(@RequestBody Person person, @PathVariable(value = "id") Long id);
	
}

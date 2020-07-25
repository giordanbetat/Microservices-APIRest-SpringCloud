package com.giordanbetat.oauth.service;

import com.giordanbetat.users.commons.models.Person;

public interface IUserService {

	public Person findByUsername(String username);
	
	public Person update(Person person, Long id);
	
}

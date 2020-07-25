package com.giordanbetat.oauth.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.giordanbetat.oauth.clients.UserFeignClient;
import com.giordanbetat.users.commons.models.Person;

import brave.Tracer;
import feign.FeignException;

@Service
public class UserService implements IUserService, UserDetailsService {

	private Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserFeignClient client;

	@Autowired 
	private Tracer tracer;
	 

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		String MSG_ERROR_LOGIN = "Erro ao tentar fazer login! O Usuario " + username + " não foi encontrado";
		
		try {
		
		Person person = client.findByUsername(username);

		List<GrantedAuthority> authorities = person.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.peek(authority -> log.info("Role: " + authority.getAuthority())).collect(Collectors.toList());

		log.info("User autenticado: " + username);

		return new User(person.getUsername(), person.getPassword(),
				person.getEnabled(), true, true, true, authorities);
		
		}catch(FeignException e){
			log.error(MSG_ERROR_LOGIN);
			
			tracer.currentSpan().tag("error.message", MSG_ERROR_LOGIN);
			
			throw new UsernameNotFoundException(MSG_ERROR_LOGIN);
		}
	}

	@Override
	public Person findByUsername(String username) {

		return client.findByUsername(username);
	}

	@Override
	public Person update(Person person, Long id) {

		return client.update(person, id);
	}

}

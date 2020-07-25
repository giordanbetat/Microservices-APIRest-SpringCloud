package com.giordanbetat.projectcloud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.giordanbetat.users.commons.models.Person;

@RepositoryRestResource(path = "persons")
public interface PersonRepository extends JpaRepository<Person, Long>{

	@RestResource(path = "search-username")
	public Person findByUsername(@Param("username") String username);
	
	@Query("select person from Person person where person.username= ?1")
	public Person searchByUsername(String username);
	
}

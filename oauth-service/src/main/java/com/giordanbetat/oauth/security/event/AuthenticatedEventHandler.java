package com.giordanbetat.oauth.security.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.giordanbetat.oauth.service.IUserService;
import com.giordanbetat.users.commons.models.Person;

import brave.Tracer;
import feign.FeignException;

@Component
public class AuthenticatedEventHandler implements AuthenticationEventPublisher {

	private Logger log = LoggerFactory.getLogger(AuthenticatedEventHandler.class);

	@Autowired
	private IUserService service;

	@Autowired
	private Tracer tracer;

	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {

		if (authentication.getName().equalsIgnoreCase("app")) {
			return;
		}

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		log.info("Succes Login " + userDetails.getUsername());

		Person person = service.findByUsername(authentication.getName());

		if (person.getChanges() != null && person.getChanges() > 0) {
			person.setChanges(0);
			service.update(person, person.getId());
		}

	}

	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		String errorMessage = "Error Login " + exception.getMessage();
		log.error(errorMessage);

		try {

			StringBuilder msg = new StringBuilder();

			msg.append(errorMessage);

			Person person = service.findByUsername(authentication.getName());

			if (person.getChanges() == null) {
				person.setChanges(0);
			}

			log.info("Tentativas de login: " + person.getChanges());

			msg.append("Tentativas de login: " + person.getChanges());

			person.setChanges(person.getChanges() + 1);

			if (person.getChanges() >= 3) {
				String maxChanges = String.format("Usuario %s desabilitado por excesso de tentativas",
						person.getUsername());
				log.error(maxChanges);
				msg.append(maxChanges);
				person.setEnabled(false);
			}

			service.update(person, person.getId());

			tracer.currentSpan().tag("error.message", msg.toString());

		} catch (FeignException e) {
			log.error(String.format("O usuario %s não foi localizado no sistema", authentication.getName()));
		}

	}
}

package com.giordanbetat.oauth.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.giordanbetat.oauth.service.IUserService;
import com.giordanbetat.users.commons.models.Person;

@Component
public class InfoAditionalToken implements TokenEnhancer {

	@Autowired
	private IUserService service;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

		Map<String, Object> info = new HashMap<String, Object>();

		Person person = service.findByUsername(authentication.getName());
		info.put("name", person.getName());
		info.put("surname", person.getSurname());
		info.put("email", person.getEmail());

		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);

		return accessToken;
	}

}

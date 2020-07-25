package com.giordanbetat.projectcloud.oauth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.DefaultOAuth2ExceptionRenderer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.OAuth2ExceptionRenderer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@RefreshScope
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Value("${config.security.oauth.jwt.key}")
	private String jwtKey;

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/api/security/oauth/**").permitAll()
				.antMatchers(HttpMethod.GET, "/api/products/findAll", "/api/items/findAll", "/api/persons/persons")
				.permitAll()
				.antMatchers(HttpMethod.GET, "/api/products/findById/{id}", "/api/items/findById/{id}/amount/{amount}",
						"/api/persons/persons/{id}")
				.hasAnyRole("ADMIN", "USER").antMatchers("/api/products/**", "/api/items/**", "/api/persons/**")
				.hasRole("ADMIN").anyRequest().authenticated().and().cors()
				.configurationSource(corsConfigurationSource()).and().exceptionHandling()
				.accessDeniedHandler(buildAccessDeniedHandler(configureExceptionRenderer())).and().exceptionHandling()
				.authenticationEntryPoint(buildAuthenticationEntryPoint(configureExceptionRenderer()));
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
		corsConfiguration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);

		return source;
	}

	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {

		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(
				new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;

	}

	@Bean
	public JwtTokenStore tokenStore() {

		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey(jwtKey);

		return tokenConverter;
	}

	protected OAuth2ExceptionRenderer configureExceptionRenderer() {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

		messageConverters.add(new MappingJackson2HttpMessageConverter(
				Jackson2ObjectMapperBuilder.json().applicationContext(this.applicationContext).build()));

		messageConverters.add(new ByteArrayHttpMessageConverter());
		StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
		stringConverter.setWriteAcceptCharset(false);
		messageConverters.add(stringConverter);
		DefaultOAuth2ExceptionRenderer renderer = new DefaultOAuth2ExceptionRenderer();
		renderer.setMessageConverters(messageConverters);
		return renderer;
	}

	protected AuthenticationEntryPoint buildAuthenticationEntryPoint(OAuth2ExceptionRenderer renderer) {
		OAuth2AuthenticationEntryPoint entryPoint = new OAuth2AuthenticationEntryPoint();
		entryPoint.setExceptionRenderer(renderer);

		return entryPoint;
	}

	protected AccessDeniedHandler buildAccessDeniedHandler(OAuth2ExceptionRenderer renderer) {
		OAuth2AccessDeniedHandler handler = new OAuth2AccessDeniedHandler();
		handler.setExceptionRenderer(renderer);

		return handler;
	}

}

package com.msg.msg.Security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		UserBuilder users = User.withDefaultPasswordEncoder();
		
		auth.inMemoryAuthentication()
			.withUser(users.username("admin").password("admin").roles("ADMIN"));
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
		.antMatchers(HttpMethod.GET, "/find/all").hasRole("ADMIN")
		.antMatchers(HttpMethod.POST, "/find/unbann-user/**").hasRole("ADMIN")
		.antMatchers(HttpMethod.POST, "/find/bann-user/**").hasRole("ADMIN")
		.and()
		.httpBasic()
		.and()
		.csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	
				
	}	

}
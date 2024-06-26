package es.upm.dit.isst.tfg.tfgwebapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.web.DefaultSecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig /* extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> */ {
	@Autowired
    private CustomAuthenticationProvider authProvider;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }


	public void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
        .authorizeHttpRequests()
			.requestMatchers("/css/**", "/img/**", "/layouts/**").permitAll()
			.requestMatchers("/", "/lista").permitAll()
			.requestMatchers("/crear", "/guardar").permitAll()
			.anyRequest().authenticated()
        .and()
            .formLogin()
				.loginPage("/login")
				.permitAll()
		.and()
            .logout()
			.permitAll();
	}
}
/*
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //NEW
        .and()
        .authorizeRequests()
			.antMatchers("/login").permitAll() // sustituye por formLogin y logout
			.antMatchers("/lista").permitAll()
            .anyRequest().authenticated()
        .and()
			.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
*/

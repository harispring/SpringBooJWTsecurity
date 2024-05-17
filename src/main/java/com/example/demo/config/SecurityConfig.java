package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.demo.filter.JwtAuthFilter;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {

        return new UserInfoUserDetailsService();
    }
    
    @Autowired
	private JwtAuthFilter authFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	
   /*     return http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/products/welcome","/new").permitAll()
                .and()
                .authorizeHttpRequests().requestMatchers("/products/**")
                .authenticated().and().formLogin().and().build(); */
    	
    	 http.csrf(csrf -> csrf.disable());
    	    http.authorizeHttpRequests(authz -> {
				try {
					authz
					        .requestMatchers("/products/welcome","/new","/products/authenticate","/authenticate").permitAll()
					        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
					        .anyRequest().authenticated()
					        .and()
					        .sessionManagement()
					        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
					        .and()
					        .authenticationProvider(authenticationProvider())
					        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
    	            );
    	    //below line is mandatory to get the h2 console view page after the login
    	    http.headers().frameOptions().disable();
    	    
    	    return http.build();
    	    
    	    
    	    /*
    	     * http.csrf(csrf -> csrf.disable());
    	    http.authorizeHttpRequests(authz -> authz
    	            .requestMatchers("/products/welcome","/new","/products/authenticate","/authenticate").permitAll()
    	            .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
    	            .anyRequest().authenticated()
    	            );
    	    //below line is mandatory to get the h2 console view page after the login
    	    http.headers().frameOptions().disable();
    	     */
    	 
    	//    return http.formLogin().and().build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}

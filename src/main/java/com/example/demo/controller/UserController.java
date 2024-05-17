package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.UserInfo;
import com.example.demo.pojo.AuthRequest;
import com.example.demo.service.UserService;
import com.example.demo.service.jwt.JwtService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtService jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/new")
	public String addNewUser(@RequestBody UserInfo userInfo) {

		return userService.addUser(userInfo);
	}

	@PostMapping("/authenticate")
	public String generateToken(@RequestBody AuthRequest authRequest) {
		//return jwtService.generateToken(authRequest.getUsername());

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		if (authentication.isAuthenticated()) {
			return jwtService.generateToken(authRequest.getUsername());
		} else {
			throw new UsernameNotFoundException("Invalid User");
		}

	}

}

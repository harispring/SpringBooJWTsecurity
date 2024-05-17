package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.UserInfo;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	
	 @Autowired
	    private PasswordEncoder passwordEncoder;
	
	public String addUser(UserInfo userInfo) {
		
		List<UserInfo> userList = userRepository.findAll();
		
		boolean userExistStatus = userList.stream().filter(x-> x.getName().equalsIgnoreCase(userInfo.getName())).findAny().isPresent();
		if(userExistStatus) {
			return "user is existed";
		}else {
			userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
			UserInfo info = (UserInfo) userRepository.save(userInfo);
			return "user is saved";
		}
	}
	

}

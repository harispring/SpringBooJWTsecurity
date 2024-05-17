package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.Product;
import com.example.demo.pojo.AuthRequest;
import com.example.demo.service.ProductService;
import com.example.demo.service.jwt.JwtService;


@RestController
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
    private ProductService service;
	
	/*
	 * @Autowired private AuthenticationManager authenticationManager;
	 */
	@Autowired
	private JwtService jwtService;
	

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }
    

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Product> getAllTheProducts() {
        return service.getProducts();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Product getProductById(@PathVariable int id) {
        return service.getProduct(id);
    }
    
    
    @PostMapping("/authenticate")
	public String generateToken(@RequestBody AuthRequest authRequest) {
    	return jwtService.generateToken(authRequest.getUsername());
		/*
		 * Authentication authentication = authenticationManager.authenticate(new
		 * UsernamePasswordAuthenticationToken(authRequest.getUsername(),
		 * authRequest.getPassword())); if(authentication.isAuthenticated()) { return
		 * jwtService.generateToken(authRequest.getUsername()); }else { throw new
		 * UsernameNotFoundException("Invalid User"); }
		 */
	}
    
    
}

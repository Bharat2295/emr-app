package net.javaguides.emrapp.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import net.javaguides.emrapp.dto.JwtAuthResponse;
import net.javaguides.emrapp.dto.LoginDto;
import net.javaguides.emrapp.dto.UserDTO;
import net.javaguides.emrapp.service.AuthService;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Build Register user REST API
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
		String result = authService.registerUser(userDTO);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
    
    // Build Login REST API
    @PostMapping("/login")	
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto){
    	JwtAuthResponse jwtAuthResponse = authService.login(loginDto);
    	return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }
}

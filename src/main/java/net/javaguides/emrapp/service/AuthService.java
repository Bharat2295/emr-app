package net.javaguides.emrapp.service;

import net.javaguides.emrapp.dto.JwtAuthResponse;
import net.javaguides.emrapp.dto.LoginDto;
import net.javaguides.emrapp.dto.UserDTO;

public interface AuthService {
    String registerUser(UserDTO userDTO);
    
    JwtAuthResponse login(LoginDto loginDto);
}
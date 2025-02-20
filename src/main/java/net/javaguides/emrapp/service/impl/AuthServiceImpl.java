package net.javaguides.emrapp.service.impl;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import net.javaguides.emrapp.dto.JwtAuthResponse;
import net.javaguides.emrapp.dto.LoginDto;
import net.javaguides.emrapp.dto.UserDTO;
import net.javaguides.emrapp.entity.Role;
import net.javaguides.emrapp.entity.Users;
import net.javaguides.emrapp.repository.RoleRepository;
import net.javaguides.emrapp.repository.UserRepository;
import net.javaguides.emrapp.security.JwtTokenProvider;
import net.javaguides.emrapp.service.AuthService;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public String registerUser(UserDTO userDTO) {
    	
        // Create a new user entity from UserDTO and save it
        Users newUser = new Users();
        newUser.setUsername(userDTO.getUsername());
        newUser.setEmail(userDTO.getEmail());
        //newUser.setPassword(userDTO.getPassword());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        
        // Retrieve the role from the database, or create it if it doesn't exist
        String roleName = userDTO.getRole();
        Role userRole = roleRepository.findByName(roleName);
        if (userRole == null) {
            userRole = new Role();
            userRole.setName(roleName);
            userRole.setUsers(new HashSet<>());
            roleRepository.save(userRole);
        }
        
        // Set roles for the new user
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        newUser.setRoles(roles);
        
        // Save the user entity
        userRepository.save(newUser);
        
        // Update the users collection in Role entity
        userRole.getUsers().add(newUser);
        roleRepository.save(userRole);
        
        return "User registered successfully!";
    }
    

	@Override
	public JwtAuthResponse login(LoginDto loginDto) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				loginDto.getUsernameOrEmail(),
				loginDto.getPassword()
		));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtTokenProvider.generateToken(authentication);
		
		Optional<Users> userOptional = userRepository.findByUsernameOrEmail(loginDto.getUsernameOrEmail(), loginDto.getUsernameOrEmail());
		
		String role = null;
		if(userOptional.isPresent()){
			Users loggedInUser = userOptional.get();
			Optional<Role> optionalRole = loggedInUser.getRoles().stream().findFirst();
			
			if(optionalRole.isPresent()) {
				Role userRole = optionalRole.get();
				role = userRole.getName();
			}
		}
			
			JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
			jwtAuthResponse.setRole(role);
			jwtAuthResponse.setAccessToken(token);
			return jwtAuthResponse;
	}
}


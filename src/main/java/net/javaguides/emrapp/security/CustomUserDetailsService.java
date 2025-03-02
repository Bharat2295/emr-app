package net.javaguides.emrapp.security;
import lombok.AllArgsConstructor;
import net.javaguides.emrapp.entity.Users;
import net.javaguides.emrapp.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
	
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException{
		
		Users user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
				.orElseThrow(() -> new UsernameNotFoundException("User not exists by Username or Email"));
		
		Set<GrantedAuthority> authorities = user.getRoles().stream()
				.map((role) -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
				.collect(Collectors.toSet());
		
		System.out.println("Loaded User customeruserDetails: " + user.getUsername() + ", Roles: " + authorities);
        
		
		return new org.springframework.security.core.userdetails.User(
				usernameOrEmail,
				user.getPassword(),
				authorities
		);
	}
}

package net.javaguides.emrapp.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import net.javaguides.emrapp.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    Optional<Users> findByUsernameOrEmail(String username, String email);
}
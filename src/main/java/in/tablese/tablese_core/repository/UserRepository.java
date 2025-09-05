package in.tablese.tablese_core.repository;

import in.tablese.tablese_core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Data JPA will automatically implement this method
    Optional<User> findByUsername(String username);
}
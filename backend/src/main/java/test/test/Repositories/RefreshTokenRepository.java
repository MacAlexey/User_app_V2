package test.test.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import test.test.Models.RefreshToken;
import test.test.Models.User;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
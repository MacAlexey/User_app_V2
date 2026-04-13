package test.test.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.test.Models.User;

import java.util.Optional;

@Repository //можно убрать*  Spring Data сам распознает JpaRepository интерфейсы.
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}

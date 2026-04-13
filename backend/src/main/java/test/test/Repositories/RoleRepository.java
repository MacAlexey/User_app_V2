package test.test.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import test.test.Models.Roles;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findByName(String name);
}

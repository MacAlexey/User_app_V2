package test.test.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import test.test.Models.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByOwnerId(Long ownerId);

    Optional<Todo> findByIdAndOwnerId(Long id, Long ownerId);
}

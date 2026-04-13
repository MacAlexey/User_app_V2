package test.test.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import test.test.Models.ToDo;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<ToDo, Long> {

    List<ToDo> findByOwnerId(Long ownerId);

    Optional<ToDo> findByIdAndOwnerId(Long id, Long ownerId);
}

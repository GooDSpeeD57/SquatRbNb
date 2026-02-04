package training.afpa.cda24060.squartrbnb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import training.afpa.cda24060.squartrbnb.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}

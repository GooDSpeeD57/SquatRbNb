package training.afpa.cda24060.squartrbnb.repository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import training.afpa.cda24060.squartrbnb.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
}
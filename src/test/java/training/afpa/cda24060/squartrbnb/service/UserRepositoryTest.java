package training.afpa.cda24060.squartrbnb.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import training.afpa.cda24060.squartrbnb.entity.Role;
import training.afpa.cda24060.squartrbnb.entity.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void shouldSaveAndFindUser() {
        // GIVEN
        Role role = new Role();
        role.setName("UTILISATEUR");
        roleRepository.save(role);

        User user = new User();
        user.setUsername("jdoe");
        user.setPassword("password");
        user.setNom("Doe");
        user.setPrenom("John");
        user.setEmail("john@doe.com");
        user.setDateNaissance(LocalDate.of(1995, 5, 5));
        user.setRole(role);

        // WHEN
        User savedUser = userRepository.save(user);
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // THEN
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("jdoe");
        assertThat(foundUser.get().getRole().getName()).isEqualTo("UTILISATEUR");
    }

    @Test
    void shouldFindAllUsers() {
        // GIVEN
        Role role = new Role();
        role.setName("ADMIN");
        roleRepository.save(role);

        User user1 = new User(null, "user1", "Nom1", "Prenom1", "u1@test.com", LocalDate.now(), null, "pwd1", null, role);
        User user2 = new User(null, "user2", "Nom2", "Prenom2", "u2@test.com", LocalDate.now(), null, "pwd2", null, role);

        userRepository.save(user1);
        userRepository.save(user2);

        // WHEN
        Iterable<User> users = userRepository.findAll();

        // THEN
        assertThat(users).hasSize(2);
    }

    @Test
    void shouldDeleteUser() {
        // GIVEN
        Role role = new Role();
        role.setName("TEST");
        roleRepository.save(role);

        User user = new User(null, "temp", "Nom", "Prenom", "temp@test.com", LocalDate.now(), null, "pwd", null, role);
        User saved = userRepository.save(user);

        // WHEN
        userRepository.deleteById(saved.getId());

        // THEN
        assertThat(userRepository.findById(saved.getId())).isEmpty();
    }
}

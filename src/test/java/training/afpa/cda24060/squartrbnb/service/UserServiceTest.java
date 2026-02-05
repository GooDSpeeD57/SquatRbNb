package training.afpa.cda24060.squartrbnb.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import training.afpa.cda24060.squartrbnb.entity.Role;
import training.afpa.cda24060.squartrbnb.entity.User;
import training.afpa.cda24060.squartrbnb.repository.RoleRepository;
import training.afpa.cda24060.squartrbnb.repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @Test
    void shouldSaveUserWithEncodedPasswordAndDefaultRole() {
        // GIVEN
        Role role = new Role(1, "UTILISATEUR", null);

        User user = new User();
        user.setUsername("jdoe");
        user.setPassword("plainPassword");
        user.setRole(null);
        user.setNom("Doe");
        user.setPrenom("John");
        user.setEmail("john@doe.com");
        user.setDateNaissance(LocalDate.of(1995, 5, 5));

        when(passwordEncoder.encode("plainPassword"))
                .thenReturn("hashedPassword");

        when(roleRepository.findByName("UTILISATEUR"))
                .thenReturn(Optional.of(role));

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        User savedUser = userService.saveUser(user);

        // THEN
        assertEquals("hashedPassword", savedUser.getPassword());
        assertEquals("UTILISATEUR", savedUser.getRole().getName());
        assertEquals("jdoe", savedUser.getUsername());

        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsMissing() {
        // GIVEN
        User user = new User();
        user.setPassword(null);

        // WHEN / THEN
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.saveUser(user)
        );

        assertEquals("Le mot de passe est obligatoire", exception.getMessage());

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldReturnUserById() {
        // GIVEN
        User user = new User();
        user.setId(1);

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        // WHEN
        Optional<User> result = userService.getUser(1);

        // THEN
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void shouldReturnAllUsers() {
        // GIVEN
        User user1 = new User();
        user1.setId(1);
        User user2 = new User();
        user2.setId(2);

        when(userRepository.findAll())
                .thenReturn(java.util.List.of(user1, user2));

        // WHEN
        Iterable<User> users = userService.getUser();

        // THEN
        int count = 0;
        for (User u : users) count++;
        assertEquals(2, count);
    }

    @Test
    void shouldDeleteUserById() {
        // WHEN
        userService.deleteUser(1);

        // THEN
        verify(userRepository).deleteById(1);
    }
}

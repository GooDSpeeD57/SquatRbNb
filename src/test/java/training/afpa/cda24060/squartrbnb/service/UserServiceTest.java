package training.afpa.cda24060.squartrbnb.service;

import io.qameta.allure.*;
import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
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

@Epic("Gestion des utilisateurs")
@Feature("Service UserService")
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
    @Story("Création d'un utilisateur")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Vérifie qu'un utilisateur est sauvegardé avec mot de passe encodé et rôle par défaut")
    @Owner("Equipe Backend")
    @DisplayName("Sauvegarde utilisateur avec password encodé et rôle UTILISATEUR")
    void shouldSaveUserWithEncodedPasswordAndDefaultRole() {

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

        User savedUser = userService.saveUser(user);

        assertEquals("hashedPassword", savedUser.getPassword());
        assertEquals("UTILISATEUR", savedUser.getRole().getName());
        assertEquals("jdoe", savedUser.getUsername());

        verify(userRepository).save(user);
    }

    @Test
    @Story("Validation des données utilisateur")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Vérifie qu'une exception est levée si le mot de passe est absent")
    @DisplayName("Exception si mot de passe manquant")
    void shouldThrowExceptionWhenPasswordIsMissing() {

        User user = new User();
        user.setPassword(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.saveUser(user)
        );

        assertEquals("Le mot de passe est obligatoire", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @Story("Consultation utilisateur")
    @Severity(SeverityLevel.NORMAL)
    @Description("Retourne un utilisateur existant par son ID")
    @DisplayName("Récupération utilisateur par ID")
    void shouldReturnUserById() {

        User user = new User();
        user.setId(1);

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        Optional<User> result = userService.getUser(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    @Story("Consultation utilisateur")
    @Severity(SeverityLevel.MINOR)
    @Description("Retourne la liste complète des utilisateurs")
    @DisplayName("Récupération de tous les utilisateurs")
    void shouldReturnAllUsers() {

        User user1 = new User();
        user1.setId(1);
        User user2 = new User();
        user2.setId(2);

        when(userRepository.findAll())
                .thenReturn(java.util.List.of(user1, user2));

        Iterable<User> users = userService.getUser();

        int count = 0;
        for (User u : users) count++;

        assertEquals(2, count);
    }

    @Test
    @Story("Suppression utilisateur")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Supprime un utilisateur via son ID")
    @DisplayName("Suppression utilisateur par ID")
    void shouldDeleteUserById() {

        userService.deleteUser(1);

        verify(userRepository).deleteById(1);
    }
}

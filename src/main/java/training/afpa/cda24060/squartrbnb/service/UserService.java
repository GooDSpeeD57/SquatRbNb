package training.afpa.cda24060.squartrbnb.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import training.afpa.cda24060.squartrbnb.entity.User;
import training.afpa.cda24060.squartrbnb.entity.Role;
import training.afpa.cda24060.squartrbnb.repository.RoleRepository;
import training.afpa.cda24060.squartrbnb.repository.UserRepository;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public Optional<User> getUser(Integer id) {
        return userRepository.findById(id);
    }

    public Iterable<User> getUser() {
        return userRepository.findAll();
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public User saveUser(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            throw new IllegalArgumentException("Le mot de passe est obligatoire");
        }

        if (user.getRole() == null) {
            Role defaultRole = roleRepository.findByName("UTILISATEUR")
                    .orElseThrow(() -> new IllegalStateException("Rôle UTILISATEUR non trouvé en BDD"));
            user.setRole(defaultRole);
        }

        return userRepository.save(user);
    }

}

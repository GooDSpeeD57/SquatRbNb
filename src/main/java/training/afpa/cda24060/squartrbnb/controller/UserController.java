package training.afpa.cda24060.squartrbnb.controller;

import org.springframework.web.bind.annotation.*;
import training.afpa.cda24060.squartrbnb.entity.User;
import training.afpa.cda24060.squartrbnb.service.UserService;

import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping("/user")
    public Iterable<User> getAllUsers() {
        return userService.getUser();
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable Integer id) {
        Optional<User>user = userService.getUser(id);
        return user.orElse(null);
    }

    @PutMapping("/user/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody User user) {
        Optional<User> userOptional = userService.getUser(id);
        if (userOptional.isPresent()) {
            User updatedUser = userOptional.get();

            if (user.getUsername() != null) {
                updatedUser.setUsername(user.getUsername());
            }
            if (user.getNom() != null) {
                updatedUser.setNom(user.getNom());
            }
            if (user.getPrenom() != null) {
                updatedUser.setPrenom(user.getPrenom());
            }
            if (user.getEmail() != null) {
                updatedUser.setEmail(user.getEmail());
            }
            if (user.getDateNaissance() != null) {
                updatedUser.setDateNaissance(user.getDateNaissance());
            }
            if (user.getPhotoPath() != null) {
                updatedUser.setPhotoPath(user.getPhotoPath());
            }
            if (user.getRole() != null) {
                updatedUser.setRole(user.getRole());
            }

            return userService.saveUser(updatedUser);
        } else {
         return null;
        }
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
}




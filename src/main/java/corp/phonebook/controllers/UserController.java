package corp.phonebook.controllers;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import lombok.AllArgsConstructor;

import java.util.List;

import corp.phonebook.service.UserService;
import corp.phonebook.data.entity.User;
import corp.phonebook.data.dto.UserDTO;

import static corp.phonebook.constants.Constants.VERSION;
import static corp.phonebook.validation.Validation.requireNull;

@RestController
@AllArgsConstructor
@RequestMapping(VERSION + "phonebooks/users")
public class UserController {

    private final UserService userService;

    // GET all owners of phonebooks  http://localhost:8080/api/v1/phonebooks/users/owners
    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/owners")
    public ResponseEntity<?> getAllOwnersOfPhonebooks(
            @AuthenticationPrincipal User user) {
        List<User> ownersOfPhonebooks = userService.getAllOwners();
        if (ownersOfPhonebooks.isEmpty()) {
            return ResponseEntity.status(404).body("No owners found.");
        } else {
            return ResponseEntity.ok(ownersOfPhonebooks);
        }
    }

    // GET user by name  http://localhost:8080/api/v1/phonebooks/users/names/Bob
    @Secured("ROLE_ADMIN")
    @GetMapping(value = {"/names/{name}"})
    public ResponseEntity<?> getUserByName(
            @PathVariable("name") String name,
            @AuthenticationPrincipal User user) {
        List<User> users = userService.getByName(name);
        if (users == null || users.isEmpty()) {
            return ResponseEntity.status(404).body("No users found with name: " + name);
        }
        return ResponseEntity.ok(users);
    }

    // GET user by id  http://localhost:8080/api/v1/phonebooks/users/2
    @GetMapping(value = "{id}")
    public ResponseEntity<?> getOne(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal User user) {
        if (!id.equals(user.getId())) {
            return ResponseEntity.status(403).body("Access denied: User ID does not match the authenticated user's ID.");
        }

        User foundUser = userService.getById(id);
        if (requireNull(foundUser)) {
            return ResponseEntity.status(404).body("User not found.");
        }

        return ResponseEntity.ok(foundUser);
    }

    // UPDATE user by id  http://localhost:8080/api/v1/phonebooks/users/1
    // {"name": "Alice + Bob" }  {"name": "Alice + Bob", "email": "newSharedEmail@gmail.com" }
    @PutMapping(value = "{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") Long id,
            @RequestBody UserDTO userDTO,
            @AuthenticationPrincipal User user) {
        if (!id.equals(user.getId())) {
            return ResponseEntity.status(403).body("Access denied: User ID does not match the authenticated user's ID.");
        }

        if (userDTO == null || (userDTO.getName() == null && userDTO.getEmail() == null)) {
            return ResponseEntity.badRequest().body("Invalid input: At least one field must be provided.");
        }

        try {
            User updatedUser = userService.update(id, userDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    // DELETE user by id  http://localhost:8080/api/v1/phonebooks/users/2
    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> delete(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal User user) {
        if (!id.equals(user.getId())) {
            return ResponseEntity.status(403).body("Access denied: You cannot delete another user.");
        }

        if (!userService.isExist(id)) {
            return ResponseEntity.status(404).body("User not found with id: " + id);
        }

        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


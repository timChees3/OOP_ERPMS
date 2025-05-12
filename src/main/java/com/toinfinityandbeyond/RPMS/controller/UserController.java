package com.toinfinityandbeyond.RPMS.controller;

import com.toinfinityandbeyond.RPMS.model.User;
import com.toinfinityandbeyond.RPMS.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController
{
    @Autowired
    private  UserService userService;

    /** SIGN UP **/
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody User user) {
        User created = userService.signup(user);
        return ResponseEntity
                .created(URI.create("/api/users/" + created.getId()))
                .body(created);
    }

    /** LOGIN **/
    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(
            @RequestBody Map<String,String> creds
    ) {
        String username = creds.get("username");
        String password = creds.get("password");
        Map<String,Object> result = userService.login(username, password);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody User userPayload)
    {
        userPayload.setId(id);
        User updated = userService.updateUser(userPayload);
        return ResponseEntity.ok(updated);  // 200
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id)
    {
        return ResponseEntity.ok(userService.getUserById(id)); // 200
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email)
    {
        return ResponseEntity.ok(userService.getUserByEmail(email)); // 200
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username)
    {
        return ResponseEntity.ok(userService.getUserByUsername(username)); // 200
    }

    /** List all users */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers()); // 200
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id)
    {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // 204
    }
}

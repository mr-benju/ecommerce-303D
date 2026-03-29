package dev.rampmaster.ecommerce.users.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.rampmaster.ecommerce.users.model.UserAccount;
import dev.rampmaster.ecommerce.users.model.LoginRequest;
import dev.rampmaster.ecommerce.users.service.UserAccountService;
import jakarta.servlet.http.HttpServletRequest;
import dev.rampmaster.ecommerce.users.config.JwtUtil;

@RestController
@RequestMapping("/api/users")
public class UserAccountController {

    private final UserAccountService service;

    public UserAccountController(UserAccountService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll(HttpServletRequest request) {

        String role = (String) request.getAttribute("role");

        if (role == null || (!role.equals("ADMIN") && !role.equals("SUPPORT"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado");
        }

        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAccount> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserAccount> create(@RequestBody UserAccount entity) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAccount> update(@PathVariable Long id, @RequestBody UserAccount entity) {
        return service.update(id, entity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request) {

        String role = (String) request.getAttribute("role");

        if (role == null || !role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado: Solo ADMIN");
        }

        if (!service.delete(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        return service.findByEmail(loginRequest.getEmail())
                .filter(user -> user.getPassword() != null
                        && user.getPassword().equals(loginRequest.getPassword()))
                .map(user -> {
                    String token = JwtUtil.generateToken(
                            user.getEmail(),
                            user.getRole().name()
                    );
                    return ResponseEntity.ok(token);
                })
                .orElseGet(() ->
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body("Credenciales incorrectas"));
    }
}
package io.github.mityavasilyev.springreactbarapp.security;

import io.github.mityavasilyev.springreactbarapp.exceptions.ExceptionController;
import io.github.mityavasilyev.springreactbarapp.security.jwt.JwtProvider;
import io.github.mityavasilyev.springreactbarapp.security.user.AppUser;
import io.github.mityavasilyev.springreactbarapp.security.user.AppUserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = AuthController.AUTH_SERVICE_PATH)
public class AuthController extends ExceptionController {

    public static final String AUTH_SERVICE_PATH = "api/auth";

    private final AuthServiceImpl authService;

    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getUsers() {
        return ResponseEntity.ok(authService.getUsers());
    }

    @GetMapping("/roles")
    public ResponseEntity<List<AppUserRole>> getRoles() {
        return ResponseEntity.ok(authService.getRoles());
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtProvider.AccessRefreshTokens> refreshTokens(@RequestBody RefreshToken refreshToken) {
        JwtProvider.AccessRefreshTokens tokens = authService.refreshTokens(refreshToken.refreshToken);
        if (tokens == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(tokens);
        }
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable("userId") int id) {
        log.info("Tried to delete user with id: {}", id);
        return null;
    }

    private record RefreshToken(String refreshToken) {
    }
}

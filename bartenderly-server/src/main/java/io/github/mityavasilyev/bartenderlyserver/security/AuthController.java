package io.github.mityavasilyev.bartenderlyserver.security;

import io.github.mityavasilyev.bartenderlyserver.exceptions.ExceptionController;
import io.github.mityavasilyev.bartenderlyserver.security.jwt.JwtConfig;
import io.github.mityavasilyev.bartenderlyserver.security.jwt.JwtProvider;
import io.github.mityavasilyev.bartenderlyserver.security.user.AppUserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = AuthController.AUTH_SERVICE_PATH)
public class AuthController extends ExceptionController {

    public static final String AUTH_SERVICE_PATH = "api/auth";

    private final AuthServiceImpl authService;
    private final JwtConfig jwtConfig;

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/users")
    public ResponseEntity<List<? extends UserDetails>> getUsers() {
        return ResponseEntity.ok(authService.getUsers());
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/roles")
    public ResponseEntity<List<AppUserRole>> getRoles() {
        return ResponseEntity.ok(authService.getRoles());
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtProvider.AccessRefreshTokens> refreshTokens(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        JwtProvider.AccessRefreshTokens tokens = authService.refreshTokens(refreshTokenDTO.refreshToken);
        if (tokens == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity
                    .ok()
                    .header(JwtConfig.getAuthorizationHeader(),
                            jwtConfig.getTokenPrefix() + tokens.accessToken())
                    .header(jwtConfig.getRefreshTokenHeader(),
                            tokens.refreshToken())
                    .build();

        }
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable("userId") int id) {
        log.info("Tried to delete user with id: {}", id);
        return null;
    }

    private record RefreshTokenDTO(String refreshToken) {
    }
}

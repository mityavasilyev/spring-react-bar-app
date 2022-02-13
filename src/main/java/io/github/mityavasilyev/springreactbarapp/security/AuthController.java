package io.github.mityavasilyev.springreactbarapp.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.mityavasilyev.springreactbarapp.exceptions.ExceptionController;
import io.github.mityavasilyev.springreactbarapp.exceptions.ExceptionUtils;
import io.github.mityavasilyev.springreactbarapp.security.role.Role;
import io.github.mityavasilyev.springreactbarapp.security.role.RoleDTO;
import io.github.mityavasilyev.springreactbarapp.security.user.AppUser;
import io.github.mityavasilyev.springreactbarapp.security.user.AppUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok(authService.getRoles());
    }

    @PostMapping("/user/save")
    public ResponseEntity<AppUser> saveUser(@RequestBody AppUserDTO appUserDTO) {
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path(AUTH_SERVICE_PATH + "/user/save").toUriString());
        return ResponseEntity
                .created(uri)
                .body(authService.saveUser(parseAppUserDTOWithRoles(appUserDTO)));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody RoleDTO roleDTO) {
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path(AUTH_SERVICE_PATH + "/role/save").toUriString());
        return ResponseEntity
                .created(uri)
                .body(authService.saveRole(roleDTO.parseRole()));
    }

    @PostMapping("/user/{userId}/assignRole/{roleId}")
    public ResponseEntity<Void> assignRoleToUser(
            @PathVariable(name = "userId") Long userId, @PathVariable(name = "roleId") Long roleId) {
        AppUser user = authService.getUser(userId);
        authService.assignRoleToUser(user.getUsername(), roleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/refresh")
    public ResponseEntity<Object> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        log.info("Refresh Token routine triggered");
        try {
            DecodedJWT decodedJWT = TokenProvider.verifyToken(authorizationHeader);
            String refreshToken = decodedJWT.getToken();

            String username = decodedJWT.getSubject();
            String issuer = request.getRequestURI().toString();
            AppUser appUser = authService.getUser(username);

            String accessToken = TokenProvider.refreshToken(appUser, issuer);

            Map<String, String> tokens = new HashMap<>();
            tokens.put(TokenProvider.JSON_FIELD_ACCESS_TOKEN, accessToken);
            tokens.put(TokenProvider.JSON_FIELD_REFRESH_TOKEN, refreshToken);
            return ResponseEntity.ok().body(tokens);

        } catch (Exception exception) {
            log.error("Error while refreshing token: {}", exception.getMessage());

            Map<String, String> error = new HashMap<>();
            error.put(ExceptionUtils.JSON_FIELD_ERROR, String.format("Failed to verify token: %s", exception.getMessage()));
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body(error);
        }
    }

    /**
     * Parses DTO object to entity and retrieves actual roles list.
     * Needed for preventing injections and relevant data
     *
     * @param appUserDTO DTO object that needs to be parsed
     * @return parsed entity
     */
    private @NotNull AppUser parseAppUserDTOWithRoles(@NotNull AppUserDTO appUserDTO) {
        AppUser appUser = appUserDTO.parseAppUser();
        Collection<Role> roles = new HashSet<>();
        Collection<RoleDTO> roleDTOS = appUserDTO.getRoles();
        if (roleDTOS != null) {
            roleDTOS.forEach(roleDTO -> roles.add(authService.getRole(roleDTO.getId())));
        }
        appUser.setRoles(roles);
        return appUser;
    }

}

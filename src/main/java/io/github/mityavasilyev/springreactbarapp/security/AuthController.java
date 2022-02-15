package io.github.mityavasilyev.springreactbarapp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mityavasilyev.springreactbarapp.exceptions.ExceptionController;
import io.github.mityavasilyev.springreactbarapp.security.role.Role;
import io.github.mityavasilyev.springreactbarapp.security.role.RoleDTO;
import io.github.mityavasilyev.springreactbarapp.security.user.AppUser;
import io.github.mityavasilyev.springreactbarapp.security.user.AppUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static io.github.mityavasilyev.springreactbarapp.security.AuthUtils.ROLES_FIELD;
import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

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
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            log.info("Refresh Token routine triggered");
            try {
                String oldRefreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = AuthUtils.getEncryptionAlgorithm();

                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(oldRefreshToken);

                String username = decodedJWT.getSubject();
                AppUser appUser = authService.getUser(username);

                String accessToken = JWT.create()
                        .withSubject(appUser.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + (10 * 60 * 1000)))
                        .withIssuer(request.getRequestURI().toString())
                        .withClaim(ROLES_FIELD,
                                appUser.getRoles().stream()
                                        .map(Role::getName)
                                        .collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", oldRefreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            } catch (Exception exception) {
                log.error("Error while refreshing token: {}", exception.getMessage());
                // TODO: 15.02.2022 Handle invalid refresh token
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
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

package io.github.mityavasilyev.springreactbarapp.security;

import io.github.mityavasilyev.springreactbarapp.exceptions.ExceptionController;
import io.github.mityavasilyev.springreactbarapp.security.user.AppUser;
import io.github.mityavasilyev.springreactbarapp.security.user.AppUserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @GetMapping("/refresh")
    public ResponseEntity<Object> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        log.info("Tried to refresh token");
        return null;
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable("userId") int id) {
        log.info("Tried to delete user with id: {}", id);
        return null;
    }

//    /**
//     * Parses DTO object to entity and retrieves actual roles list.
//     * Needed for preventing injections and relevant data
//     *
//     * @param appUserDTO DTO object that needs to be parsed
//     * @return parsed entity
//     */
//    private @NotNull AppUser parseAppUserDTOWithRoles(@NotNull AppUserDTO appUserDTO) {
//        AppUser appUser = appUserDTO.parseAppUser();
//        Collection<AppUserPermission> appUserAuthorities = new HashSet<>();
//        Collection<RoleDTO> roleDTOS = appUserDTO.getRoles();
//        if (roleDTOS != null) {
//            roleDTOS.forEach(roleDTO -> appUserAuthorities.add(authService.getRole(roleDTO.getId())));
//        }
//        appUser.setRoles(appUserAuthorities);
//        return appUser;
//    }

}

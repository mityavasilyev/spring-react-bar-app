package io.github.mityavasilyev.springreactbarapp.security;

import io.github.mityavasilyev.springreactbarapp.exceptions.ExceptionController;
import io.github.mityavasilyev.springreactbarapp.security.appuser.AppUser;
import io.github.mityavasilyev.springreactbarapp.security.appuser.AppUserDTO;
import io.github.mityavasilyev.springreactbarapp.security.role.Role;
import io.github.mityavasilyev.springreactbarapp.security.role.RoleDTO;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/auth")
public class AuthController extends ExceptionController {

    private final AuthServiceImpl authService;

    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getUsers() {
        return ResponseEntity.ok(authService.getUsers());
    }

    @PostMapping("/user/save")
    public ResponseEntity<AppUser> saveUser(@RequestBody AppUserDTO appUserDTO) {
        return ResponseEntity.ok(
                authService.saveUser(
                        parseAppUserDTOWithRoles(appUserDTO)));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody RoleDTO roleDTO) {
        return ResponseEntity.ok(authService.saveRole(roleDTO.parseRole()));
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

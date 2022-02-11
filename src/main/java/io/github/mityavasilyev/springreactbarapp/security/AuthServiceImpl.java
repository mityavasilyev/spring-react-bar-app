package io.github.mityavasilyev.springreactbarapp.security;

import io.github.mityavasilyev.springreactbarapp.security.appuser.AppUser;
import io.github.mityavasilyev.springreactbarapp.security.appuser.AppUserRepository;
import io.github.mityavasilyev.springreactbarapp.security.role.Role;
import io.github.mityavasilyev.springreactbarapp.security.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;

    @Override
    public AppUser saveUser(AppUser appUser) {
        log.info("Saving user to the database: id[{}] username[{}]", appUser.getId(), appUser.getUsername());
        return appUserRepository.save(appUser);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving role to the database: id[{}] name[{}]", role.getId(), role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void assignRoleToUser(String username, Long roleId) {
        log.info("Assigning role with id {} to user {}", username, roleId.toString());
        AppUser user = appUserRepository.findByUsername(username);
        if (user == null) throw new ResponseStatusException(NOT_FOUND, "No user with such id");
        Optional<Role> role = roleRepository.findById(roleId);
        if (role.isPresent()) {
            user.getRoles().add(role.get());
        } else {
            throw new ResponseStatusException(NOT_FOUND, "No role with such id");
        }
    }

    @Override
    public AppUser getUser(Long id) {
        log.info("Fetching user by id {}", id.toString());
        Optional<AppUser> appUser = appUserRepository.findById(id);
        if (appUser.isPresent()) {
            return appUser.get();
        } else {
            throw new ResponseStatusException(NOT_FOUND, "No user with such id");
        }
    }

    @Override
    public Role getRole(Long id) {
        log.info("Fetching role by id {}", id.toString());
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {
            return role.get();
        } else {
            throw new ResponseStatusException(NOT_FOUND, "No role with such id");
        }
    }

    @Override
    public List<AppUser> getUsers() {
        log.info("Fetching all users");
        return appUserRepository.findAll();
    }
}

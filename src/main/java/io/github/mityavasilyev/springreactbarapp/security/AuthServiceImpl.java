package io.github.mityavasilyev.springreactbarapp.security;

import io.github.mityavasilyev.springreactbarapp.security.user.AppUser;
import io.github.mityavasilyev.springreactbarapp.security.user.AppUserRepository;
import io.github.mityavasilyev.springreactbarapp.security.user.AppUserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUser saveUser(AppUser appUser) {
        log.info("Saving user to the database: id[{}] username[{}]", appUser.getId(), appUser.getUsername());
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return appUserRepository.save(appUser);
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
    public AppUser getUser(String username) {
        log.info("Fetching user by username {}", username);
        Optional<AppUser> appUser = appUserRepository.findByUsername(username);
        if (appUser.isPresent()) {
            return appUser.get();
        } else {
            throw new ResponseStatusException(NOT_FOUND, "No user with such username");
        }
    }

    @Override
    public List<AppUser> getUsers() {
        log.info("Fetching all users");
        return new ArrayList<>(appUserRepository.findAll());
    }

    @Override
    public List<AppUserRole> getRoles() {
        log.info("Fetching all roles");
        List<AppUserRole> appUserRoles = Arrays.stream(AppUserRole.values()).toList();
        return appUserRoles;
    }

    /**
     * Binds Spring Security users to AppUsers
     *
     * @param username to search for in db
     * @return User details
     * @throws UsernameNotFoundException no user with such username was found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository
                .findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("No such user: %s", username))
                );

    }
}

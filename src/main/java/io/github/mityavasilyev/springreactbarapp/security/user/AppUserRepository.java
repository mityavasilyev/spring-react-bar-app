package io.github.mityavasilyev.springreactbarapp.security.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByUsername(String username);
    List<AppUser> findByUsernameContainingIgnoreCase(String username);
}

package io.github.mityavasilyev.springreactbarapp.security;

import io.github.mityavasilyev.springreactbarapp.security.user.AppUser;
import io.github.mityavasilyev.springreactbarapp.security.user.AppUserRole;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AuthService extends UserDetailsService {

    AppUser saveUser(AppUser appUser);

    AppUser getUser(Long id);

    AppUser getUser(String username);

    List<AppUser> getUsers();

    List<AppUserRole> getRoles();

}

package io.github.mityavasilyev.springreactbarapp.security;

import io.github.mityavasilyev.springreactbarapp.security.user.AppUser;
import io.github.mityavasilyev.springreactbarapp.security.role.Role;

import java.util.List;

public interface AuthService {

    AppUser saveUser(AppUser appUser);

    Role saveRole(Role role);

    void assignRoleToUser(String username, Long roleId);

    AppUser getUser(Long id);

    AppUser getUser(String username);

    Role getRole(Long id);

    List<AppUser> getUsers();

    List<Role> getRoles();

}

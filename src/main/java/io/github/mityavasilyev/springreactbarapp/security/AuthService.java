package io.github.mityavasilyev.springreactbarapp.security;

import io.github.mityavasilyev.springreactbarapp.security.appuser.AppUser;
import io.github.mityavasilyev.springreactbarapp.security.role.Role;

import java.util.List;

public interface AuthService {

    AppUser saveUser(AppUser appUser);

    Role saveRole(Role role);

    void assignRoleToUser(String username, Long roleId);

    AppUser getUser(Long id);

    Role getRole(Long id);

    List<AppUser> getUsers();

}

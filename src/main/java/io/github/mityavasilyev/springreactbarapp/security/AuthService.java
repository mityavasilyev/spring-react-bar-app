package io.github.mityavasilyev.springreactbarapp.security;

import io.github.mityavasilyev.springreactbarapp.exceptions.NoSuchRoleException;
import io.github.mityavasilyev.springreactbarapp.exceptions.NoSuchUserException;
import io.github.mityavasilyev.springreactbarapp.security.appuser.AppUser;
import io.github.mityavasilyev.springreactbarapp.security.role.Role;

import java.util.List;

public interface AuthService {

    AppUser saveUser(AppUser appUser);
    Role saveRole(Role role);
    void assignRoleToUser(String username, Long roleId) throws NoSuchRoleException, NoSuchUserException;
    AppUser getUser(Long id) throws NoSuchUserException;
    Role getRole(Long id) throws NoSuchRoleException;
    List<AppUser> getUsers();

}

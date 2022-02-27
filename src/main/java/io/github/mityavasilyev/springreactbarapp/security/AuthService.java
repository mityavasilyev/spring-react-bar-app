package io.github.mityavasilyev.springreactbarapp.security;

import io.github.mityavasilyev.springreactbarapp.exceptions.InvalidUserException;
import io.github.mityavasilyev.springreactbarapp.security.jwt.JwtProvider;
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

    String updateUserRefreshToken(String username, String refreshToken) throws InvalidUserException;

    JwtProvider.AccessRefreshTokens refreshTokens(String refreshToken);

}

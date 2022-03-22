package io.github.mityavasilyev.bartenderlyserver.security;

import io.github.mityavasilyev.bartenderlyserver.exceptions.InvalidUserException;
import io.github.mityavasilyev.bartenderlyserver.security.jwt.JwtProvider;
import io.github.mityavasilyev.bartenderlyserver.security.user.AppUser;
import io.github.mityavasilyev.bartenderlyserver.security.user.AppUserRole;
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

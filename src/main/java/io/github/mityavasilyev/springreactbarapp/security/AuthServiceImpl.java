package io.github.mityavasilyev.springreactbarapp.security;

import io.github.mityavasilyev.springreactbarapp.security.jwt.JwtConfig;
import io.github.mityavasilyev.springreactbarapp.security.jwt.JwtProvider;
import io.github.mityavasilyev.springreactbarapp.security.user.AppUser;
import io.github.mityavasilyev.springreactbarapp.security.user.AppUserRepository;
import io.github.mityavasilyev.springreactbarapp.security.user.AppUserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
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
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    /**
     * Saves provided user
     *
     * @param appUser User to save
     * @return  Saved user
     */
    @Override
    public AppUser saveUser(AppUser appUser) {
        log.info("Saving user to the database: id[{}] username[{}]", appUser.getId(), appUser.getUsername());
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return appUserRepository.save(appUser);
    }

    /**
     * Gets user with matching id
     *
     * @param id ID of user to search for
     * @return  AppUser with provided ID if found. Throws ResponseStatusException otherwise
     */
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

    /**
     * Gets user with matching username
     *
     * @param username Username of user to search for
     * @return  AppUser with provided username if found. Throws ResponseStatusException otherwise
     */
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

    /**
     * Gets all users
     *
     * @return List of all users
     */
    @Override
    public List<AppUser> getUsers() {
        log.info("Fetching all users");
        return appUserRepository.findAll();
    }

    /**
     * Gets all roles
     *
     * @return List of all roles
     */
    @Override
    public List<AppUserRole> getRoles() {
        log.info("Fetching all roles");
        List<AppUserRole> appUserRoles = Arrays.stream(AppUserRole.values()).toList();
        return appUserRoles;
    }

    @Override
    public String updateUserRefreshToken(String username, String refreshToken) {
        log.info("Updated refresh-token for user [{}]", username);
        // TODO: 24.02.2022 validate username from token
        AppUser appUser = getUser(username);
        appUser.setActiveRefreshToken(refreshToken);
        appUserRepository.save(appUser);
        return appUser.getActiveRefreshToken();
    }

    @Override
    public JwtProvider.AccessRefreshTokens refreshTokens(String refreshToken) {

        // TODO: 24.02.2022 Handle errors and exceptions
        // Parsing JWT
        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(refreshToken);

        // Building user from token
        Claims body = claimsJws.getBody();
        String username = body.getSubject();
        log.info("User [{}] requested new tokens via refresh-token", username);

        // Checking user's issued refresh token
        AppUser appUser = getUser(username);
        if (!appUser.getActiveRefreshToken().equals(refreshToken)) {
            log.warn("Provided token [{}] do not matches one stored in db", refreshToken);
            return null;
        }

        // Issuing new tokens
        try {
            JwtProvider.AccessRefreshTokens accessRefreshTokens = JwtProvider.generateTokens(
                    loadUserByUsername(username),
                    secretKey,
                    jwtConfig.getAccessTokenExpirationAfterHours(),
                    jwtConfig.getRefreshTokenExpirationAfterDays()
            );

            // Updating user's refresh token
            String newRefreshToken = updateUserRefreshToken(username, accessRefreshTokens.refreshToken());
            return new JwtProvider.AccessRefreshTokens(accessRefreshTokens.accessToken(), newRefreshToken);

        } catch (ResponseStatusException e) {
            log.error("No such user: [{}]", username);
            return null;
        }
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

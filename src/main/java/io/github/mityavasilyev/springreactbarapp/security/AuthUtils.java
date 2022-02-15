package io.github.mityavasilyev.springreactbarapp.security;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;

@UtilityClass
public class AuthUtils {

    // Pass down this value from somewhere
    private static final String SECRET = "secret";
    public static final String ROLES_FIELD = "roles";

    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static Algorithm getEncryptionAlgorithm() {
        return Algorithm.HMAC256(SECRET.getBytes(StandardCharsets.UTF_8));
    }

}

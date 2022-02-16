package io.github.mityavasilyev.springreactbarapp.security.utils;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.experimental.UtilityClass;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;

@UtilityClass
public class AuthUtils {

    // Pass down this value from somewhere

    public static final String ROLES_FIELD = "roles";

}

package io.github.mityavasilyev.springreactbarapp.security;

import io.github.mityavasilyev.springreactbarapp.security.jwt.JwtConfig;
import io.github.mityavasilyev.springreactbarapp.security.jwt.JwtTokenVerificationFilter;
import io.github.mityavasilyev.springreactbarapp.security.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,  // Enables @pre/post usages
        securedEnabled = true,  // Checks if @secured must be enabled
        jsr250Enabled = true)   // Allows @roleAllowed usage
@RequiredArgsConstructor
public class AuthConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    /**
     * Configures rules by which http server would operate
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                    .ignoringAntMatchers("/login", "/api/auth/refresh")          // No need for CSRF when logging in
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // Cookie based
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)   // Stop tracking sessions since using JWTs
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(             // Authenticate user
                        authenticationManager(),
                        authService,
                        jwtConfig,
                        secretKey))
                .addFilterAfter(new JwtTokenVerificationFilter(                        // Verify provided JWT if there's one
                                secretKey,
                                jwtConfig),
                        JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()                                                         // Configuring path access rules
                .antMatchers("/login", "/api/auth/refresh").permitAll()
//                .antMatchers("/api/auth/**").hasRole(AppUserRole.ADMIN.name())
                .anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);   // Providing default password encoder
        provider.setUserDetailsService(authService);    // Providing access to users repository

        return provider;
    }
}

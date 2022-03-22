package io.github.mityavasilyev.bartenderlyserver.security.user;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static io.github.mityavasilyev.bartenderlyserver.security.user.AppUserPermission.*;

/**
 * Contains Roles that are preconfigured permission bundles
 */
public enum AppUserRole {

    BARTENDER(Sets.newHashSet(
            COCKTAILS_READ,
            COCKTAILS_WRITE,
            TAGS_READ,
            TAGS_WRITE,
            PRODUCTS_READ,
            PRODUCTS_WRITE)),

    ADMIN(Arrays.stream(AppUserPermission.values()).collect(Collectors.toSet()));

    private final Set<AppUserPermission> permissions;

    AppUserRole(Set<AppUserPermission> permissions) {
        this.permissions = permissions;
    }

    /**
     * @return Enum Permissions that are bundled in role
     */
    public Set<AppUserPermission> getPermissions() {
        return permissions;
    }

    /**
     * @return Role and bundled permissions that are converted to GrantedAuthorities
     */
    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> authorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}

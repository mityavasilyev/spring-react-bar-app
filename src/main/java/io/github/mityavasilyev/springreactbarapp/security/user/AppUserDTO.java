package io.github.mityavasilyev.springreactbarapp.security.user;

import io.github.mityavasilyev.springreactbarapp.security.role.RoleDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
public class AppUserDTO {

    private String name;
    private String username;
    private String password;
    private Collection<RoleDTO> roles = new ArrayList<>();

    /**
     * Roles injection needed
     * Security feature. Parses model to entity. Prevents from injection and misuse of new/update methods
     *
     * @return parsed entity
     */
    public AppUser parseAppUser() {
        return AppUser.builder()
                .name(this.name)
                .username(this.username)
                .password(this.password)
                .build();
    }
}

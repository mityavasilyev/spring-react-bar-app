package io.github.mityavasilyev.springreactbarapp.security.role;

import lombok.*;

@Data
@NoArgsConstructor
public class RoleDTO {

    private Long id;
    private String name;

    /**
     * Security feature. Parses model to entity. Prevents from injection and misuse of new/update methods
     *
     * @return parsed entity
     */
    public Role parseRole() {
        return new Role(null, this.name);
    }
}

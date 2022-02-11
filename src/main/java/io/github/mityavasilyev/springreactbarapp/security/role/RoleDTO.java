package io.github.mityavasilyev.springreactbarapp.security.role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
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
        return new Role(this.id, this.name);
    }
}

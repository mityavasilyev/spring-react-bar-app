package io.github.mityavasilyev.springreactbarapp.security.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findByNameContainingIgnoreCase(String name);
}

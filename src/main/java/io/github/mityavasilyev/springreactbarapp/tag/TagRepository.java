package io.github.mityavasilyev.springreactbarapp.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * Searches repository for tags that have Name containing provided string
     *
     * @param name
     * @return matching cocktails
     */
    List<Tag> findByNameContainingIgnoreCase(String name);
}

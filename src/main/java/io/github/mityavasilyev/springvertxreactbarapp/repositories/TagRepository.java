package io.github.mityavasilyev.springvertxreactbarapp.repositories;

import io.github.mityavasilyev.springvertxreactbarapp.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}

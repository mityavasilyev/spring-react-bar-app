package io.github.mityavasilyev.springvertxreactbarapp.repositories;

import io.github.mityavasilyev.springvertxreactbarapp.model.Cocktail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CocktailRepository extends JpaRepository<Cocktail, Long> {
}

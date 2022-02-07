package io.github.mityavasilyev.springreactbarapp.cocktail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CocktailRepository extends JpaRepository<Cocktail, Long> {

    /**
     * Searches repository for cocktails that have Name containing provided string
     *
     * @param name
     * @return matching cocktails
     */
    List<Cocktail> findByNameContainingIgnoreCase(String name);

    /**
     * Searches repository for cocktails that have tag with matching id
     *
     * @param tagId
     * @return cocktails that have tag with matching id
     */
    @Query("select c from cocktail c, tag t where t.id = ?1 and t member of c.tags ")
    List<Cocktail> findCocktailsByTagId(Long tagId);

}

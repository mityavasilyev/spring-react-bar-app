package io.github.mityavasilyev.springvertxreactbarapp.services;

import io.github.mityavasilyev.springvertxreactbarapp.model.Cocktail;
import io.github.mityavasilyev.springvertxreactbarapp.repositories.CocktailRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CocktailService {

    private final CocktailRepository cocktailRepository;

    public CocktailService(CocktailRepository cocktailRepository) {
        this.cocktailRepository = cocktailRepository;
    }

    /**
     * @return all cocktails
     */
    public List<Cocktail> getAll() {
        return cocktailRepository.findAll();
    }

    /**
     * @param id cocktail id
     * @return cocktail with matching id
     */
    public Cocktail getById(Long id) {
        return cocktailRepository.findById(id).get();
    }

    /**
     * @param tagId id of the tag
     * @return cocktails with matching tag id
     */
    public List<Cocktail> getAllByTagId(Long tagId) {
        return cocktailRepository.findCocktailsByTagId(tagId);
    }

    /**
     * @param name string to search for
     * @return cocktails names of which contain provided string
     */
    public List<Cocktail> getAllByName(String name) {
        return cocktailRepository.findByNameContainingIgnoreCase(name);
    }


}

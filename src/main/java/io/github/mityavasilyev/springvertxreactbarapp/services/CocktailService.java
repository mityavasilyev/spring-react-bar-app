package io.github.mityavasilyev.springvertxreactbarapp.services;

import io.github.mityavasilyev.springvertxreactbarapp.model.Cocktail;
import io.github.mityavasilyev.springvertxreactbarapp.repositories.CocktailRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

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
        Optional<Cocktail> cocktail = cocktailRepository.findById(id);
        if (cocktail.isPresent()) {
            return cocktail.get();
        } else {
            throw new ResponseStatusException(NOT_FOUND, "No cocktail with such id");
        }
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

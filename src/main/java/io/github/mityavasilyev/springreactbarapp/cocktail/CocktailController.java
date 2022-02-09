package io.github.mityavasilyev.springreactbarapp.cocktail;

import io.github.mityavasilyev.springreactbarapp.exceptions.ExceptionController;
import io.github.mityavasilyev.springreactbarapp.extra.Ingredient;
import io.github.mityavasilyev.springreactbarapp.extra.Recipe;
import io.github.mityavasilyev.springreactbarapp.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "api/cocktails")
public class CocktailController extends ExceptionController {
    private final CocktailService cocktailService;

    public CocktailController(CocktailService cocktailService) {
        this.cocktailService = cocktailService;
    }

    @GetMapping
    public List<Cocktail> getAllCocktails() {
        return cocktailService.getAll();
    }

    @GetMapping(path = "/name/{cocktailName}")
    public List<Cocktail> getAllCocktailsByName(@PathVariable("cocktailName") String name) {
        return cocktailService.getAllByName(name);
    }

    @GetMapping(path = "/tagId/{tagId}")
    public List<Cocktail> getAllCocktailsByTagId(@PathVariable("tagId") Long id) {
        return cocktailService.getAllByTagId(id);
    }

    @GetMapping(path = "{cocktailId}")
    public Cocktail getCocktailById(@PathVariable("cocktailId") Long id) {
        return cocktailService.getById(id);
    }

    @PostMapping
    public ResponseEntity addNewCocktail(@RequestBody CocktailDTO cocktailDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cocktailService.addNew(cocktailDTO.parseCocktail()));
    }

    @DeleteMapping(path = "{cocktailId}")
    public void deleteCocktail(@PathVariable("cocktailId") Long id) {
        cocktailService.deleteById(id);
    }

    // TODO: 31.01.2022 Update mappings to use ResponseEntity
    @PatchMapping(path = "{cocktailId}")
    public ResponseEntity<Cocktail> updateCocktail(@PathVariable("cocktailId") Long id,
                                                   @RequestBody CocktailDTO cocktailDTO) {
        Cocktail cocktailPatch = cocktailDTO.parseCocktail();
        Cocktail cocktail = cocktailService.getById(id);
        if (cocktail == null) return ResponseEntity.notFound().build();

        if (cocktailPatch.getName() != null) cocktail.setName(cocktailPatch.getName());
        if (cocktailPatch.getDescription() != null) cocktail.setDescription(cocktailPatch.getDescription());
        if (cocktailPatch.getIngredients() != null) cocktail.setIngredients(cocktailPatch.getIngredients());
        if (cocktailPatch.getRecipe() != null) cocktail.setRecipe(cocktailPatch.getRecipe());
        if (cocktailPatch.getNote() != null) cocktail.setNote(cocktailPatch.getNote());
        if (cocktailPatch.getTags() != null) cocktail.setTags(cocktailPatch.getTags());
        cocktail = cocktailService.updateById(id, cocktail);

        return ResponseEntity.ok(cocktail);
    }
}

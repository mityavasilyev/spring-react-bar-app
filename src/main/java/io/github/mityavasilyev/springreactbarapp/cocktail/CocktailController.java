package io.github.mityavasilyev.springreactbarapp.cocktail;

import io.github.mityavasilyev.springreactbarapp.exceptions.ExceptionController;
import io.github.mityavasilyev.springreactbarapp.tag.Tag;
import io.github.mityavasilyev.springreactbarapp.tag.TagDTO;
import io.github.mityavasilyev.springreactbarapp.tag.TagService;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "api/cocktails")
public class CocktailController extends ExceptionController {

    private final CocktailService cocktailService;

    private final TagService tagService;

    public CocktailController(CocktailService cocktailService, TagService tagService) {
        this.cocktailService = cocktailService;
        this.tagService = tagService;
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
    public ResponseEntity<Cocktail> addNewCocktail(@RequestBody CocktailDTO cocktailDTO) {
        Cocktail cocktail = parseCocktailDTOWithTags(cocktailDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cocktailService.addNew(cocktail));
    }

    @DeleteMapping(path = "{cocktailId}")
    public void deleteCocktail(@PathVariable("cocktailId") Long id) {
        cocktailService.deleteById(id);
    }

    // TODO: 31.01.2022 Update mappings to use ResponseEntity
    @PatchMapping(path = "{cocktailId}")
    public ResponseEntity<Cocktail> updateCocktail(@PathVariable("cocktailId") Long id,
                                                   @RequestBody CocktailDTO cocktailDTO) {
        Cocktail cocktailPatch = parseCocktailDTOWithTags(cocktailDTO);
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

    /**
     * Parses DTO object to entity and retrieves actual tag list.
     * Needed for preventing injections and relevant data
     *
     * @param cocktailDTO DTO object that needs to be parsed
     * @return parsed entity
     */
    private @NotNull Cocktail parseCocktailDTOWithTags(@NotNull CocktailDTO cocktailDTO) {
        Cocktail cocktail = cocktailDTO.parseCocktail();
        Set<Tag> tags = new HashSet<>();
        Set<TagDTO> tagDTOS = cocktailDTO.getTags();
        if (tagDTOS != null) {
            tagDTOS.forEach(tag -> {
                tags.add(tagService.getById(tag.parseTagWithId().getId()));
            });
        }
        cocktail.setTags(tags);
        return cocktail;
    }
}

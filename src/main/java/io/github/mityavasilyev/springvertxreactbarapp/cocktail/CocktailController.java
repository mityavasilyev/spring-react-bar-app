package io.github.mityavasilyev.springvertxreactbarapp.cocktail;

import org.hibernate.PropertyValueException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(path = "api/cocktails")
public class CocktailController {
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
    public void addNewCocktail(@RequestBody Cocktail cocktail) {
        cocktailService.addNew(cocktail);
    }

    /**
     * Handles exceptions
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Exception ex,
                                         HttpServletRequest request, HttpServletResponse response) {
        if (ex instanceof NullPointerException) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (ex instanceof DataIntegrityViolationException) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

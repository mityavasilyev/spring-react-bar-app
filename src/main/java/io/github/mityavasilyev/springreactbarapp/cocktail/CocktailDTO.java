package io.github.mityavasilyev.springreactbarapp.cocktail;

import io.github.mityavasilyev.springreactbarapp.extra.Ingredient;
import io.github.mityavasilyev.springreactbarapp.extra.Recipe;
import io.github.mityavasilyev.springreactbarapp.tag.Tag;
import io.github.mityavasilyev.springreactbarapp.tag.TagDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CocktailDTO {

    String name;
    String description;
    Set<TagDTO> tags;
    Set<Ingredient> ingredients;
    Recipe recipe;
    String note;

    /**
     * Security feature. Parses model to entity. Prevents from injection and misuse of new/update methods
     *
     * @return parsed entity
     */
    public Cocktail parseCocktail() {
        return Cocktail.builder()
                .name(this.name)
                .description(this.description)
                .ingredients(this.ingredients)
                .recipe(this.recipe)
                .note(this.note)
                .build();
    }
}

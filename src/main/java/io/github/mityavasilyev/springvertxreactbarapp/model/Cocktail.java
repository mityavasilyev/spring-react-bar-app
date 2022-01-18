package io.github.mityavasilyev.springvertxreactbarapp.model;

import lombok.*;

import java.util.Set;

/**
 * Cocktail class that contains Name, Description, Tags,
 * Ingredients, Recipe and Note
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(includeFieldNames = false)
public class Cocktail {

    private Long id;
    private String name;
    private String description;
    private Set<Tag> tags;
    private Set<Ingredient> ingredients;
    private Recipe recipe;
    private String note;

}

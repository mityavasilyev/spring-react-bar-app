package io.github.mityavasilyev.springvertxreactbarapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Stores steps of the recipe for a cocktail
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {

    private Long id;
    private Map<Long, String> steps;
}

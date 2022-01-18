package io.github.mityavasilyev.springvertxreactbarapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Map;

/**
 * Stores steps of the recipe for a cocktail
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "recipe")
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            updatable = false
    )
    private Long id;

    @Column(
            name = "steps",
            columnDefinition = "TEXT"
    )
    private String steps;
}
